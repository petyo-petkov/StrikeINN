package com.example.strikeinn.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.strikeinn.network.F1ApiClient
import com.example.strikeinn.network.models.Drivers
import com.example.strikeinn.network.models.Intervals
import com.example.strikeinn.network.models.Position
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

class DataScreenViewModel(
    private val apiClient: F1ApiClient
) : ViewModel() {


    private val _uiState = MutableStateFlow<DataScreenUIState>(DataScreenUIState.Idle)
    val uiState: StateFlow<DataScreenUIState> = _uiState

    private var realtimeDataJob: Job? = null

    init {
        loadLiveDriverData()
    }

    fun loadLiveDriverData(
        sessionKeyParam: String? = "latest",
        meetingKeyParam: String? = "latest",
        year: Int? = null,
        circuit: String? = null,
        event: String? = null
    ) {
        _uiState.value = DataScreenUIState.Loading
        realtimeDataJob?.cancel()

        realtimeDataJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val session = apiClient.getSessions(
                    sessionKey = sessionKeyParam,
                    meetingKey = meetingKeyParam,
                    year = year,
                    circuitShortName = circuit,
                    sessionName = event
                ).firstOrNull()

                val actualSessionKey = session?.session_key.toString()
                val actualMeetingKey = session?.meeting_key.toString()
                val sessionEndDate = parseEndDate(
                    session?.date_end,
                    session?.gmt_offset
                )

                val meeting = apiClient.getMeetings(
                    meetingKey = actualMeetingKey
                ).firstOrNull()

                val drivers = apiClient.getDrivers(
                    sessionKey = actualSessionKey,
                    meetingKey = actualMeetingKey
                )

                val eventInfo = EventInfo(
                    date = session?.date_start.orEmpty(),
                    meetingName = meeting?.meeting_official_name.orEmpty(),
                    sessionName = session?.session_name.orEmpty(),
                    country = session?.country_name.orEmpty(),
                    circuit = session?.circuit_short_name.orEmpty()
                )

                if (sessionEndDate != null && ZonedDateTime.now().isAfter(sessionEndDate)) {
                    val statiIntervals = apiClient.getStaticIntervals(
                        sessionKey = actualSessionKey,
                        meetingKey = actualMeetingKey
                    )
                    val staticPositions = apiClient.getStaticPositions(
                        sessionKey = actualSessionKey,
                        meetingKey = actualMeetingKey
                    )

                    _uiState.value = DataScreenUIState.Success(
                        driverInfoList = processData(drivers, statiIntervals, staticPositions),
                        eventInfo = eventInfo
                    )

                } else {
                    combine(
                        apiClient.getIntervals(
                            sessionKey = actualSessionKey,
                            meetingKey = actualMeetingKey,
                        ),
                        apiClient.getPosition(
                            sessionKey = actualSessionKey,
                            meetingKey = actualMeetingKey,
                        )
                    ) { intervals, positions ->
                        DataScreenUIState.Success(
                            driverInfoList = processData(drivers, intervals, positions),
                            eventInfo = eventInfo
                        )
                    }
                        .cancellable()                      //????
                        .distinctUntilChanged()             //????
                        .collectLatest { uiState ->
                            _uiState.value = uiState
                        }
                }

            } catch (e: Exception) {
                _uiState.value = DataScreenUIState.Error(
                    e.message ?: "Error al cargar los datos en tiempo real"
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        realtimeDataJob?.cancel()
    }

    fun refreshData() {
        loadLiveDriverData()
    }
}

private fun processData(
    drivers: List<Drivers>,
    intervals: List<Intervals>,
    positions: List<Position>
): List<DriverInfo> {
    val driverInfoMap = drivers.associateByTo(
        mutableMapOf(),
        { it.driver_number },
        {
            DriverInfo(
                driverName = it.name_acronym,
                teamColor = it.team_colour,
                driverNumber = it.driver_number
            )
        }
    )
    positions
        .groupBy { it.driver_number }
        .forEach { (driverNumber, posList) ->
            posList.maxByOrNull { it.date }?.let { latestPosition ->
                println("Driver $driverNumber: Position=${latestPosition.position}")  // PRINT ....
                driverInfoMap[driverNumber]?.let { currentInfo ->
                    driverInfoMap[driverNumber] = currentInfo.copy(
                        position = latestPosition.position
                    )
                }
            }
        }

    intervals
        .groupBy { it.driver_number }
        .forEach { (driverNumber, intList) ->
            if (driverNumber == -1) return@forEach
            intList.maxByOrNull { it.date }?.let { latestInterval ->
                driverInfoMap[driverNumber]?.let { currentInfo ->
                    val gap = latestInterval.gapToLeaderValue
                    val interval = latestInterval.intervalValue
                    println("Driver $driverNumber: Gap=$gap, Interval=$interval")  //PRINT .....
                    driverInfoMap[driverNumber] = currentInfo.copy(
                        gap = gap,
                        interval = interval
                    )
                }
            }
        }
    return driverInfoMap.values.toList().sortedBy { it.position }
}

/*
private fun processData(
    drivers: List<Drivers>,
    intervals: List<Intervals>,
    positions: List<Position>
): List<DriverInfo> {
    val driverInfoMap = mutableMapOf<Int, DriverInfo>()
    drivers.forEach { driver ->
        driverInfoMap[driver.driver_number] = DriverInfo(
            driverName = driver.name_acronym,
            teamColor = driver.team_colour,
            driverNumber = driver.driver_number
        )
    }
    val latestPositions = positions
        .groupBy { it.driver_number }
        .mapValues { (_, posList) ->
            posList.maxByOrNull { it.date }
        }

    latestPositions.forEach { (driverNumber, posData) ->
        if (posData != null) {
            driverInfoMap[driverNumber]?.let { currentInfo ->
                driverInfoMap[driverNumber] = currentInfo.copy(
                    position = posData.position
                )

            }
        }
    }
    val latestIntervals = intervals
        .groupBy { it.driver_number }
        .mapValues { (_, intList) ->
            intList.maxByOrNull { it.date }
        }
    latestIntervals.forEach { (driverNumber, intData) ->
        if (intData != null && driverNumber != -1) {
            val currentInfo =
                driverInfoMap[driverNumber]
            if (currentInfo != null) {
                val gap = extractJsonValue(intData.gap_to_leader)
                val interval = extractJsonValue(intData.interval)

                driverInfoMap[driverNumber] = currentInfo.copy(
                    gap = gap,
                    interval = interval
                )
            }
        }
    }
    return driverInfoMap.values.toList().sortedBy { it.position }
}

 */

sealed class DataScreenUIState {
    object Idle : DataScreenUIState()
    object Loading : DataScreenUIState()
    data class Error(val message: String) : DataScreenUIState()
    data class Success(
        val driverInfoList: List<DriverInfo>,
        val eventInfo: EventInfo
    ) : DataScreenUIState()

}

@Serializable
data class DriverInfo(
    val position: Int? = -1,
    val driverName: String = "",
    val teamColor: String = "",
    val interval: String = "",
    val gap: String = "",
    val driverNumber: Int
)

@Serializable
data class EventInfo(
    val date: String = "",
    val meetingName: String = "",
    val sessionName: String = "",
    val country: String = "",
    val circuit: String = ""
)


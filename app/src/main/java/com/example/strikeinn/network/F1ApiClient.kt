package com.example.strikeinn.network

import com.example.strikeinn.network.models.CarData
import com.example.strikeinn.network.models.Drivers
import com.example.strikeinn.network.models.Intervals
import com.example.strikeinn.network.models.Laps
import com.example.strikeinn.network.models.Location
import com.example.strikeinn.network.models.Meetings
import com.example.strikeinn.network.models.Pit
import com.example.strikeinn.network.models.Position
import com.example.strikeinn.network.models.RaceControl
import com.example.strikeinn.network.models.Sessions
import com.example.strikeinn.network.models.Stints
import com.example.strikeinn.network.models.TeamRadio
import com.example.strikeinn.network.models.Weather
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext


class F1ApiClient(
    private val client: HttpClient
) : F1Api {

    private val carDataCache = Cache<CarData>()
    private val intervalsCache = Cache<Intervals>()
    private val lapsCache = Cache<Laps>()
    private val locationsCache = Cache<Location>()
    private val pitCache = Cache<Pit>()
    private val positionCache = Cache<Position>()
    private val raceControlCache = Cache<RaceControl>()
    private val stintsCache = Cache<Stints>()
    private val teamRadiosCache = Cache<TeamRadio>()
    private val weatherCache = Cache<Weather>()
    private val driverCache = Cache<Drivers>()
    private val sessionsCache = Cache<Sessions>()
    private val meetingsCache = Cache<Meetings>()

    companion object {
        private const val BASE_URL = "https://api.openf1.org/v1"
        private const val DELAY_TIME = 4500L
        private const val CACHE_DURATION = 3500L
    }

    private suspend inline fun <reified T> makeRequest(
        endpoint: String, crossinline block: URLBuilder.() -> Unit
    ): List<T> {
        try {
            val response = client.get("$BASE_URL/$endpoint") {
                url { block() }
                headers {
                    append(HttpHeaders.Accept, "application/json")
                    append(HttpHeaders.AcceptCharset, "UTF-8")
                    append(HttpHeaders.CacheControl, "no-cache")
                    append(HttpHeaders.Connection, "keep-alive")
                    append(HttpHeaders.UserAgent, "F1ApiClient/1.0")
                    append(HttpHeaders.AcceptLanguage, "en-US,en;q=0.9")
                    append(HttpHeaders.Pragma, "no-cache")
                }
            }
            if (response.status == HttpStatusCode.OK) {
                return response.body<List<T>>()
            } else {
                println("Error $response")
                return emptyList()
            }

        } catch (e: Exception) {
            println("Error al obtener los datos: ${e.message}")
            return emptyList()
        }
    }

    override fun getCarData(
        date: String?,
        driverNumber: Int?,
        meetingKey: String?,
        sessionKey: String?,
    ): Flow<List<CarData>> = flow {
        while (coroutineContext.isActive) {

            val cachedData = carDataCache.get(CACHE_DURATION)
            if (cachedData != null) {
                emit(cachedData)
            }
            try {
                val request: List<CarData> = makeRequest("car_data") {
                    date?.let { parameters.append("date", it) }
                    driverNumber?.let { parameters.append("driver_number", it.toString()) }
                    meetingKey?.let { parameters.append("meeting_key", it) }
                    sessionKey?.let { parameters.append("session_key", it) }
                }
                carDataCache.put(request)
                emit(request)
            } catch (e: Exception) {
                println("Error fetching car data: ${e.message}")
            }

            delay(DELAY_TIME)

        }

    }

    override suspend fun getDrivers(
        driverNumber: Int?,
        meetingKey: String?,
        sessionKey: String?
    ): List<Drivers> {
        val cachedData = driverCache.get(CACHE_DURATION)
        if (cachedData != null) {
            return cachedData
        }
        val drivers: List<Drivers> = makeRequest("drivers") {
            driverNumber?.let { parameters.append("driver_number", it.toString()) }
            meetingKey?.let { parameters.append("meeting_key", it) }
            sessionKey?.let { parameters.append("session_key", it) }
        }
        driverCache.put(drivers)
        return drivers
    }


    override fun getIntervalsFlow(
        sessionKey: String?,
        meetingKey: String?,
        driverNumber: Int?,
    ): Flow<List<Intervals>> = flow {
        while (coroutineContext.isActive) {

            val cachedData = intervalsCache.get(CACHE_DURATION)
            if (cachedData != null) {
                emit(cachedData)
            }
            try {
                val request: List<Intervals> = makeRequest("intervals") {
                    sessionKey?.let { parameters.append("session_key", it) }
                    meetingKey?.let { parameters.append("meeting_key", it) }
                    driverNumber?.let { parameters.append("driver_number", it.toString()) }
                }
                intervalsCache.put(request)
                emit(request)
            } catch (e: Exception) {
                println("Error fetching car data: ${e.message}")
            }
            delay(DELAY_TIME)

        }
    }

    override fun getLaps(
        sessionKey: String?, meetingKey: String?, driverNumber: Int?, lapNumber: Int?
    ): Flow<List<Laps>> = flow {
        while (coroutineContext.isActive) {

            val cachedData = lapsCache.get(CACHE_DURATION)
            if (cachedData != null) {
                emit(cachedData)
            }
            try {
                val request: List<Laps> = makeRequest("laps") {
                    sessionKey?.let { parameters.append("session_key", it) }
                    meetingKey?.let { parameters.append("meeting_key", it) }
                    driverNumber?.let { parameters.append("driver_number", it.toString()) }
                    lapNumber?.let { parameters.append("lap_number", it.toString()) }
                }
                lapsCache.put(request)
                emit(request)
            } catch (e: Exception) {
                println("Error fetching car data: ${e.message}")
            }
            delay(DELAY_TIME)

        }
    }

    override fun getLocations(
        date: String?, driverNumber: Int?, sessionKey: String?, meetingKey: String?
    ): Flow<List<Location>> = flow {
        while (coroutineContext.isActive) {

            val cachedData = locationsCache.get(CACHE_DURATION)
            if (cachedData != null) {
                emit(cachedData)
            }
            try {
                val request: List<Location> = makeRequest("location") {
                    date?.let { parameters.append("date", it) }
                    driverNumber?.let { parameters.append("driver_number", it.toString()) }
                    sessionKey?.let { parameters.append("session_key", it) }
                    meetingKey?.let { parameters.append("meeting_key", it) }
                }
                locationsCache.put(request)
                emit(request)
            } catch (e: Exception) {
                println("Error fetching car data: ${e.message}")
            }
            delay(DELAY_TIME)

        }
    }

    override suspend fun getSessions(
        sessionKey: String?,
        meetingKey: String?,
        sessionName: String?,
        countryName: String?,
        circuitShortName: String?,
        year: Int?
    ): List<Sessions> {
        val cachedData = sessionsCache.get(CACHE_DURATION)
        if (cachedData != null) {
            return cachedData
        }
        val sessions: List<Sessions> = makeRequest("sessions") {
            sessionKey?.let { parameters.append("session_key", it) }
            meetingKey?.let { parameters.append("meeting_key", it) }
            sessionName?.let { parameters.append("session_name", it) }
            countryName?.let { parameters.append("country_name", it) }
            circuitShortName?.let { parameters.append("circuit_short_name", it) }
            year?.let { parameters.append("year", it.toString()) }
        }
        sessionsCache.put(sessions)
        return sessions
    }

    override suspend fun getMeetings(
        year: Int?,
        countryName: String?,
        location: String?,
        meetingKey: String?
    ): List<Meetings> {
        val cachedData = meetingsCache.get(CACHE_DURATION)
        if (cachedData != null) {
            return cachedData
        }
        val meetings: List<Meetings> = makeRequest("meetings") {
            year?.let { parameters.append("year", it.toString()) }
            countryName?.let { parameters.append("country_name", it) }
            location?.let { parameters.append("location", it) }
            meetingKey?.let { parameters.append("meeting_key", it) }
        }
        meetingsCache.put(meetings)
        return meetings
    }

    override fun getPit(
        driverNumber: Int?,
        sessionKey: String?,
        meetingKey: String?,
    ): Flow<List<Pit>> = flow {
        while (coroutineContext.isActive) {

            val cachedData = pitCache.get(CACHE_DURATION)
            if (cachedData != null) {
                emit(cachedData)
            }
            try {
                val request: List<Pit> = makeRequest("pit") {
                    sessionKey?.let { parameters.append("session_key", it) }
                    meetingKey?.let { parameters.append("meeting_key", it) }
                    driverNumber?.let { parameters.append("driver_number", it.toString()) }
                }
                pitCache.put(request)
                emit(request)
            } catch (e: Exception) {
                println("Error fetching car data: ${e.message}")
            }
            delay(DELAY_TIME)

        }
    }


    override fun getPositionFlow(
        driverNumber: Int?, sessionKey: String?, meetingKey: String?
    ): Flow<List<Position>> = flow {
        while (coroutineContext.isActive) {

            val cachedData = positionCache.get(CACHE_DURATION)
            if (cachedData != null) {
                emit(cachedData)
            }
            try {
                val request: List<Position> = makeRequest("position") {
                    sessionKey?.let { parameters.append("session_key", it) }
                    meetingKey?.let { parameters.append("meeting_key", it) }
                    driverNumber?.let { parameters.append("driver_number", it.toString()) }
                }
                positionCache.put(request)
                emit(request)
            } catch (e: Exception) {
                println("Error fetching car data: ${e.message}")
            }
            delay(DELAY_TIME)

        }
    }

    override fun getRaceControl(
        driverNumber: Int?, sessionKey: String?, meetingKey: String?
    ): Flow<List<RaceControl>> = flow {
        while (coroutineContext.isActive) {

            val cachedData = raceControlCache.get(CACHE_DURATION)
            if (cachedData != null) {
                emit(cachedData)
            }
            try {
                val request: List<RaceControl> = makeRequest("race_control") {
                    sessionKey?.let { parameters.append("session_key", it) }
                    meetingKey?.let { parameters.append("meeting_key", it) }
                    driverNumber?.let { parameters.append("driver_number", it.toString()) }

                }
                raceControlCache.put(request)
                emit(request)
            } catch (e: Exception) {
                println("Error fetching car data: ${e.message}")
            }
            delay(DELAY_TIME)

        }
    }

    override fun getStints(
        driverNumber: Int?, sessionKey: String?, meetingKey: String?
    ): Flow<List<Stints>> = flow {
        while (coroutineContext.isActive) {

            val cachedData = stintsCache.get(CACHE_DURATION)
            if (cachedData != null) {
                emit(cachedData)
            }
            try {
                val request: List<Stints> = makeRequest("stints") {
                    sessionKey?.let { parameters.append("session_key", it) }
                    meetingKey?.let { parameters.append("meeting_key", it) }
                    driverNumber?.let { parameters.append("driver_number", it.toString()) }
                }
                stintsCache.put(request)
                emit(request)
            } catch (e: Exception) {
                println("Error fetching car data: ${e.message}")
            }
            delay(DELAY_TIME)

        }
    }

    override fun getTeamRadios(
        driverNumber: Int?, sessionKey: String?, meetingKey: String?
    ): Flow<List<TeamRadio>> = flow {
        while (coroutineContext.isActive) {
            val cachedData = teamRadiosCache.get(CACHE_DURATION)
            if (cachedData != null) {
                emit(cachedData)
            }
            try {
                val request: List<TeamRadio> = makeRequest("team_radio") {
                    sessionKey?.let { parameters.append("session_key", it) }
                    meetingKey?.let { parameters.append("meeting_key", it) }
                    driverNumber?.let { parameters.append("driver_number", it.toString()) }
                }
                teamRadiosCache.put(request)
                emit(request)
            } catch (e: Exception) {
                println("Error fetching car data: ${e.message}")
            }
            delay(DELAY_TIME)

        }
    }

    override fun getWeather(
        sessionKey: String?,
        meetingKey: String?
    ): Flow<List<Weather>> = flow {
        while (coroutineContext.isActive) {

            val cachedData = weatherCache.get(CACHE_DURATION)
            if (cachedData != null) {
                emit(cachedData)
            }
            try {
                val request: List<Weather> = makeRequest("weather") {
                    sessionKey?.let { parameters.append("session_key", it) }
                    meetingKey?.let { parameters.append("meeting_key", it) }
                }
                weatherCache.put(request)
                emit(request)
            } catch (e: Exception) {
                println("Error fetching car data: ${e.message}")
            }

            delay(DELAY_TIME)

        }
    }

    override suspend fun refresh() {
        carDataCache.invalidate()
        intervalsCache.invalidate()
        lapsCache.invalidate()
        locationsCache.invalidate()
        pitCache.invalidate()
        positionCache.invalidate()
        raceControlCache.invalidate()
        stintsCache.invalidate()
        teamRadiosCache.invalidate()
        weatherCache.invalidate()
        driverCache.invalidate()

    }


}
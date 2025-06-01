package com.example.strikeinn.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.strikeinn.R
import com.example.strikeinn.network.circuits
import com.example.strikeinn.network.eventTypes
import com.example.strikeinn.network.years
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun App() {
    val vm = koinViewModel<DataScreenViewModel>()
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val eventInfo by vm.eventInfo.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,

        ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = uiState) {
                is DataScreenUIState.Idle -> "Esperando datos..."
                is DataScreenUIState.Loading -> LoadingIndicator()
                is DataScreenUIState.Error -> stringResource(R.string.error_grave)
                is DataScreenUIState.Success -> {
                    val driverInfoList = currentState.driverInfoList

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        // Info del Evnto......................................................

                        Header(
                            eventName = eventInfo.eventName,
                            date = eventInfo.date,
                            eventType = eventInfo.eventType,
                            onClick = {
                                showBottomSheet = true
                            })

                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomEnd = 16.dp,
                                bottomStart = 16.dp
                            ),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(
                                1.dp, color = MaterialTheme.colorScheme.onSurface
                            ),
                        ) {

                            //Driver position, team color, driver name........................

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Spacer(modifier = Modifier.width(80.dp))

                                Text(
                                    text = "Interval",
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .weight(1f)
                                )

                                Text(
                                    text = "Gap",
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .weight(1f)
                                )

                            }
                            driverInfoList.let { drivers ->
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp, bottom = 12.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    items(drivers, key = { it.driverNumber }) { driverInfo ->
                                        DriverListItem(
                                            position = driverInfo.position.toString(),
                                            teamColor = driverInfo.teamColor,
                                            driverName = driverInfo.driverName,
                                            interval = driverInfo.interval,
                                            gap = driverInfo.gap
                                        )
                                        HorizontalDivider()
                                    }
                                }
                            }


                        }


                    }

                }


            }
        }
        if (showBottomSheet) {
            BottomSheet(
                onDismiss = {
                    showBottomSheet = false
                }, onOKClick = { year, circuit, event ->
                    vm.loadLiveDriverData(
                        sessionKey = null,
                        meetingKey = null,
                        year = year,
                        circuit = circuit,
                        event = event
                    )
                },
                onRefreshClick = vm::refreshData
            )
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    onOKClick: (year: Int, circuit: String, event: String) -> Unit,
    onRefreshClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    var selectedYear by remember { mutableStateOf("2025") }
    var selectedCircuit by remember { mutableStateOf("Selecciona una opción") }
    var selectedEventType by remember { mutableStateOf("Selecciona una opción") }


    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        sheetMaxWidth = 680.dp,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        scrimColor = Color.Transparent,
    ) {

        RaceFilterBottomSheet(
            selectedYear = selectedYear,
            onYearSelected = { selectedYear = it },
            selectedCircuit = selectedCircuit,
            onCircuitSelected = { selectedCircuit = it },
            selectedRaceType = selectedEventType,
            onRaceTypeSelected = { selectedEventType = it },
            years = years,
            circuits = circuits,
            raceTypes = eventTypes,
            onDismiss = { onDismiss() },
            onOkClick = {
                onOKClick(
                    selectedYear.toInt(), selectedCircuit, selectedEventType
                )
            },
            onRefreshClick = { onRefreshClick() })

    }

}


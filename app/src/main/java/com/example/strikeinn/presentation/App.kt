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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.strikeinn.R
import com.example.strikeinn.viewModel.DataScreenUIState
import com.example.strikeinn.viewModel.DataScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun App() {
    val vm = koinViewModel<DataScreenViewModel>()
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        vm.loadLiveDriverData()
    }

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
                is DataScreenUIState.Loading -> Indicator()
                is DataScreenUIState.Error -> stringResource(R.string.error_grave)
                is DataScreenUIState.Success -> {
                    val driverInfoList = currentState.driverInfoList
                    val eventInfo = currentState.eventInfo

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        // Info del Evnto......................................................
                        Header(
                            eventName = eventInfo.meetingName,
                            date = eventInfo.date,
                            sessionName = eventInfo.sessionName,
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
                                    text = "Gap",
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .weight(1f)
                                )

                                Text(
                                    text = "Interval",
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
                vm = vm,
                onDismiss = {
                    showBottomSheet = false
                },
                onOKClick = { year, circuit, event ->
                    vm.loadLiveDriverData(
                        sessionKeyParam = null,
                        meetingKeyParam = null,
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


package com.example.strikeinn.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.strikeinn.network.years
import com.example.strikeinn.viewModel.DataScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    vm: DataScreenViewModel,
    onDismiss: () -> Unit,
    onOKClick: (year: Int, circuit: String, event: String) -> Unit,
    onRefreshClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    val listCircuits by vm.listCircuits.collectAsStateWithLifecycle()
    val listEvents by vm.listEventsState.collectAsStateWithLifecycle()

    var selectedYear by remember { mutableStateOf("2025") }
    var selectedCircuit by remember { mutableStateOf("Selecciona primero el año") }
    var selectedEventType by remember { mutableStateOf("Selecciona primero el circuito") }

    LaunchedEffect(key1 = selectedYear, key2 = selectedCircuit, key3 = selectedEventType) {
        vm.fetchCircuits(selectedYear.toInt())
        vm.fetchEvents(selectedCircuit)
    }

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        sheetMaxWidth = 680.dp,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        scrimColor = Color.Transparent,
    ) {

        RaceFilterDropDownMenu(
            selectedYear = selectedYear,
            onYearSelected = { selectedYear = it },
            selectedCircuit = selectedCircuit,
            onCircuitSelected = { selectedCircuit = it },
            selectedRaceType = selectedEventType,
            onRaceTypeSelected = { selectedEventType = it },
            years = years,
            circuits = listCircuits,
            raceTypes = listEvents,
            onDismiss = { onDismiss() },
            onOkClick = {
                onOKClick(
                    selectedYear.toInt(), selectedCircuit, selectedEventType
                )
            },
            onRefreshClick = { onRefreshClick() })

    }

}


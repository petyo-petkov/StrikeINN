package com.example.strikeinn.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun RaceFilterDropDownMenu(
    selectedYear: String,
    onYearSelected: (String) -> Unit,
    selectedCircuit: String,
    onCircuitSelected: (String) -> Unit,
    selectedRaceType: String,
    onRaceTypeSelected: (String) -> Unit,
    years: List<String>,
    circuits: List<String>,
    raceTypes: List<String>,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DropdownSelector(
            label = "Año",
            options = years,
            selectedOption = selectedYear,
            onOptionSelected = onYearSelected
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownSelector(
            label = "Circuito",
            options = circuits,
            selectedOption = selectedCircuit,
            onOptionSelected = onCircuitSelected
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownSelector(
            label = "Evento",
            options = raceTypes,
            selectedOption = selectedRaceType,
            onOptionSelected = onRaceTypeSelected
        )
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    onOkClick()
                    onDismiss()
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("OK")
            }
            Button(
                onClick = {
                    onRefreshClick()
                    onDismiss()
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Refresh")
            }
        }
    }
}

@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Text(label, fontSize = 12.sp)

        OutlinedButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )

        ) {
            Text(selectedOption)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 300.dp),
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),

                    )
            }
        }
    }
}




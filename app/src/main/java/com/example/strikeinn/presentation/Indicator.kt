package com.example.strikeinn.presentation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Indicator() {
    LoadingIndicator(
        modifier = Modifier
            .size(120.dp),
        color = MaterialTheme.colorScheme.primary,
    )
}
package com.example.strikeinn.viewModel

import kotlinx.serialization.Serializable

@Serializable
data class DriverInfo(
    val position: Int? = -1,
    val driverName: String = "",
    val teamColor: String = "",
    val interval: String = "",
    val gap: String = "",
    val driverNumber: Int
)

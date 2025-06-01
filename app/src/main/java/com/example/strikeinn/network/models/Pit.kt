package com.example.strikeinn.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Pit(
    val date: String,
    val driver_number: Int,
    val lap_number: Int,
    val meeting_key: Int,
    val pit_duration: Double,
    val session_key: Int
)
package com.example.strikeinn.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Stints(
    val compound: String,
    val driver_number: Int,
    val lap_end: Int,
    val lap_start: Int,
    val meeting_key: Int,
    val session_key: Int,
    val stint_number: Int,
    val tyre_age_at_start: Int
)
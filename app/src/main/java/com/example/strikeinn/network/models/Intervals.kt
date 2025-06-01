package com.example.strikeinn.network.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Intervals(
    val date: String,
    val driver_number: Int,
    val gap_to_leader: JsonElement,
    val interval: JsonElement,
    val meeting_key: Int,
    val session_key: Int
)
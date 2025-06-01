package com.example.strikeinn.network.models

import kotlinx.serialization.Serializable

@Serializable
data class TeamRadio(
    val date: String,
    val driver_number: Int,
    val meeting_key: Int,
    val recording_url: String,
    val session_key: Int
)
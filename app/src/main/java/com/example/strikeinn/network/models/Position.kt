package com.example.strikeinn.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val date: String,
    val driver_number: Int,
    val meeting_key: Int,
    val position: Int,
    val session_key: Int
)
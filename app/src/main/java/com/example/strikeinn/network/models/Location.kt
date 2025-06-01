package com.example.strikeinn.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val date: String,
    val driver_number: Int,
    val meeting_key: Int,
    val session_key: Int,
    val x: Int,
    val y: Int,
    val z: Int
)
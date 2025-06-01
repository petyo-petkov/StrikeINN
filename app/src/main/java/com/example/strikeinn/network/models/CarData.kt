package com.example.strikeinn.network.models

import kotlinx.serialization.Serializable

@Serializable
data class CarData(
    val brake: Int,
    val date: String,
    val driver_number: Int,
    val drs: Int,
    val meeting_key: Int,
    val n_gear: Int,
    val rpm: Int,
    val session_key: Int,
    val speed: Int,
    val throttle: Int
)
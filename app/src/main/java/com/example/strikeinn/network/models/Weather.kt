package com.example.strikeinn.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val air_temperature: Double,
    val date: String,
    val humidity: Double,
    val meeting_key: Int,
    val pressure: Double,
    val rainfall: Int,
    val session_key: Int,
    val track_temperature: Double,
    val wind_direction: Int,
    val wind_speed: Double
)
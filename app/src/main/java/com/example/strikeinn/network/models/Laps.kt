package com.example.strikeinn.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Laps(
    val date_start: String,
    val driver_number: Int,
    val duration_sector_1: Double,
    val duration_sector_2: Double,
    val duration_sector_3: Double,
    val i1_speed: Int,
    val i2_speed: Int,
    val is_pit_out_lap: Boolean,
    val lap_duration: Double,
    val lap_number: Int,
    val meeting_key: Int,
    val segments_sector_1: List<Int>,
    val segments_sector_2: List<Int>,
    val segments_sector_3: List<Int>,
    val session_key: Int,
    val st_speed: Int
)
package com.example.strikeinn.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Meetings(
    val circuit_key: Int,
    val circuit_short_name: String,
    val country_code: String,
    val country_key: Int,
    val country_name: String,
    val date_start: String,
    val gmt_offset: String,
    val location: String,
    val meeting_key: Int,
    val meeting_name: String,
    val meeting_official_name: String,
    val year: Int
)
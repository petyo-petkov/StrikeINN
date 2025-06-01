package com.example.strikeinn.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Drivers(
    val broadcast_name: String,
    val country_code: String? = null,
    val driver_number: Int,
    val first_name: String,
    val full_name: String,
    val headshot_url: String? = null,
    val last_name: String,
    val meeting_key: Int,
    val name_acronym: String,
    val session_key: Int,
    val team_colour: String,
    val team_name: String
)
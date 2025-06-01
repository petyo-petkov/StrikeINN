package com.example.strikeinn.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RaceControl(
    val category: String? = null,
    val date: String? = null,
    val driver_number: Int? = null,
    val flag: String? = null,
    val lap_number: Int? = null,
    val meeting_key: Int? = null,
    val message: String? = null,
    val scope: String? = null,
    val sector: Int? = null,
    val session_key: Int? = null,

)
package com.example.strikeinn.viewModel

import kotlinx.serialization.Serializable

@Serializable
data class EventInfo(
    val date: String = "",
    val meetingName: String = "",
    val sessionName: String = "",
    val country: String = "",
    val circuit: String = ""
)

package com.example.strikeinn.network.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class Intervals(
    val date: String,
    val driver_number: Int,
    val gap_to_leader: JsonElement,
    val interval: JsonElement,
    val meeting_key: Int,
    val session_key: Int
) {
    val gapToLeaderValue: String
        get() = parseDynamicValue(gap_to_leader)

    val intervalValue: String
        get() = parseDynamicValue(interval)

    private fun parseDynamicValue(element: JsonElement): String {
        return when (element) {
            is JsonNull -> "-"
            is JsonPrimitive -> element.jsonPrimitive.content
            else -> "N/A"
        }
    }

}
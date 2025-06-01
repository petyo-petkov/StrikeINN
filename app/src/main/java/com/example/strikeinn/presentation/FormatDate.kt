package com.example.strikeinn.presentation

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formatDate(dateString: String?): String {
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss")
    val isoFormatter = DateTimeFormatter.ISO_DATE_TIME

    return if (dateString.isNullOrEmpty()){
        "Fecha no disponible"
    } else {
        try {
            LocalDateTime.parse(dateString, isoFormatter).format(outputFormatter)
        } catch (e: Exception) {
            "$dateString ${ e.message }"
        }
    }

}
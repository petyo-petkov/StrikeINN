package com.example.strikeinn.presentation

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private val LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME

private val OFFSET_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME


fun parseEndDate(dateEndString: String?, gmtOffsetString: String?): ZonedDateTime? {
    if (dateEndString.isNullOrBlank()) return null

    return try {
        try {
            return ZonedDateTime.parse(dateEndString, OFFSET_DATE_TIME_FORMATTER)
        } catch (e: DateTimeParseException) {
            println("Error al parsear $dateEndString , ${e.message}")
        }
        val localDateTime = LocalDateTime.parse(dateEndString, LOCAL_DATE_TIME_FORMATTER)
        if (!gmtOffsetString.isNullOrBlank()) {
            val offset = ZoneOffset.of(gmtOffsetString)
            OffsetDateTime.of(localDateTime, offset).atZoneSameInstant(ZoneId.systemDefault())
        } else {
            println("Advertencia: gmt_offset no proporcionado para date_end '$dateEndString'. Asumiendo zona horaria del sistema.")
            localDateTime.atZone(ZoneId.systemDefault())
        }
    } catch (e: DateTimeParseException) {
        println("Error al parsear date_end: '$dateEndString' con offset '$gmtOffsetString'. ${e.message}")
        null
    } catch (e: Exception) {
        println("Error al construir ZonedDateTime para date_end: '$dateEndString', offset: '$gmtOffsetString'. ${e.message}")
        null
    }
}
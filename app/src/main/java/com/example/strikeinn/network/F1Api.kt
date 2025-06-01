package com.example.strikeinn.network

import com.example.strikeinn.network.models.CarData
import com.example.strikeinn.network.models.Drivers
import com.example.strikeinn.network.models.Intervals
import com.example.strikeinn.network.models.Laps
import com.example.strikeinn.network.models.Location
import com.example.strikeinn.network.models.Meetings
import com.example.strikeinn.network.models.Pit
import com.example.strikeinn.network.models.Position
import com.example.strikeinn.network.models.RaceControl
import com.example.strikeinn.network.models.Sessions
import com.example.strikeinn.network.models.Stints
import com.example.strikeinn.network.models.TeamRadio
import com.example.strikeinn.network.models.Weather
import kotlinx.coroutines.flow.Flow

interface F1Api {
    fun getCarData(
        date: String? = null,
        driverNumber: Int? = null,
        meetingKey: String? = null,
        sessionKey: String? = null,
    ): Flow<List<CarData>>

    suspend fun getDrivers(
        driverNumber: Int? = null,
        meetingKey: String? = null,
        sessionKey: String? = null,
    ): List<Drivers>

    fun getIntervalsFlow(
        sessionKey: String? = null,
        meetingKey: String? = null,
        driverNumber: Int? = null,
    ): Flow<List<Intervals>>

    fun getLaps(
        sessionKey: String? = null,
        meetingKey: String? = null,
        driverNumber: Int? = null,
        lapNumber: Int? = null
    ): Flow<List<Laps>>

    suspend fun getSessions(
        sessionKey: String? = null,
        meetingKey: String? = null,
        sessionName: String? = null,
        countryName: String? = null,
        circuitShortName: String? = null,
        year: Int? = null,
    ): List<Sessions>

    fun getLocations(
        date: String? = null,
        driverNumber: Int? = null,
        sessionKey: String? = null,
        meetingKey: String? = null,
    ): Flow<List<Location>>

    suspend fun getMeetings(
        year: Int? = null,
        countryName: String? = null,
        location: String? = null,
        meetingKey: String? = null,
    ): List<Meetings>

    fun getPit(
        driverNumber: Int? = null,
        sessionKey: String? = null,
        meetingKey: String? = null,
    ): Flow<List<Pit>>

    fun getPositionFlow(
        driverNumber: Int? = null,
        sessionKey: String? = null,
        meetingKey: String? = null,
    ): Flow<List<Position>>

    fun getRaceControl(
        driverNumber: Int? = null,
        sessionKey: String? = null,
        meetingKey: String? = null,
    ): Flow<List<RaceControl>>

    fun getStints(
        driverNumber: Int? = null,
        sessionKey: String? = null,
        meetingKey: String? = null,
    ): Flow<List<Stints>>

    fun getTeamRadios(
        driverNumber: Int? = null,
        sessionKey: String? = null,
        meetingKey: String? = null,
    ): Flow<List<TeamRadio>>

    fun getWeather(
        sessionKey: String? = null,
        meetingKey: String? = null,
    ): Flow<List<Weather>>

    suspend fun refresh()


}
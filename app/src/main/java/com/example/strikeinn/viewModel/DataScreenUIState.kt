package com.example.strikeinn.viewModel

sealed class DataScreenUIState {
    object Idle : DataScreenUIState()
    object Loading : DataScreenUIState()
    data class Error(val message: String) : DataScreenUIState()
    data class Success(
        val driverInfoList: List<DriverInfo>,
        val eventInfo: EventInfo
    ) : DataScreenUIState()

}
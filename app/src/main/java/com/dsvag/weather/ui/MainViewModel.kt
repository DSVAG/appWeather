package com.dsvag.weather.ui

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsvag.weather.data.repositorys.ForecastRepository
import com.dsvag.weather.models.forecast.ForecastResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val forecastRepository: ForecastRepository,
) : ViewModel() {

    @Volatile
    private var state = State()

    private val _stateFlow = MutableStateFlow(state)
    val stateFlow = _stateFlow.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.wtf(TAG, throwable)

        state = state.copy(
            errorType = when (throwable) {
                is IOException -> ErrorType.Network
                is HttpException -> ErrorType.Server
                else -> ErrorType.Other
            }
        )

        updateState()
    }

    fun fetchForecast(location: Location?) {
        viewModelScope.launch(exceptionHandler) {

            state = if (location != null) {
                state.copy(forecast = forecastRepository.fetchForecast(location.longitude, location.latitude))
            } else {
                state.copy(errorType = ErrorType.Location)
            }

            updateState()
        }
    }

    fun setPermissionsStatus(status: Boolean) {
        state = state.copy(permissionStatus = status)
        updateState()
    }

    fun setNetworkStatus(status: Boolean) {
        state = state.copy(networkStatus = status)
        updateState()
    }

    fun setError(errorType: ErrorType?) {
        state = state.copy(errorType = errorType)
        updateState()
    }

    private fun updateState() {
        _stateFlow.value = state
        Log.d(TAG, state.toString())
    }

    data class State(
        val permissionStatus: Boolean = false,
        val networkStatus: Boolean = false,
        val forecast: ForecastResponse? = null,
        val errorType: ErrorType? = null,
    )

    enum class ErrorType {
        Network, Server, Location, Permissions, Other
    }

    private companion object {
        const val TAG = "MainViewModel"
    }
}
package com.dsvag.weather.data.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dsvag.weather.data.di.getAppComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FragmentViewModel(application: Application) : ViewModel() {

    private val forecastRepository = application.getAppComponent().forecastRepository

    val forecast = forecastRepository.getForecastFromDb().asLiveData()

    private val locationRepository = application.getAppComponent().locationRepository

    fun onLocationPermissionGranted() {
        viewModelScope.launch {
            locationRepository
                .fetchLocation()
                .onEach { forecastRepository.fetchForecast(it.latitude, it.longitude) }
                .collect { }

        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FragmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FragmentViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}
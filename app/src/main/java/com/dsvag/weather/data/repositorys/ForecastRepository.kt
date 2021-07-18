package com.dsvag.weather.data.repositorys

import com.dsvag.weather.data.remote.OwmApiService
import com.dsvag.weather.models.forecast.ForecastResponse
import javax.inject.Inject

class ForecastRepository @Inject constructor(
    private val owmApiService: OwmApiService,
) {

    suspend fun fetchForecast(latitude: Double, longitude: Double, units: String = "metric"): ForecastResponse {
        return owmApiService.getForecast(latitude, longitude, units)
    }

}
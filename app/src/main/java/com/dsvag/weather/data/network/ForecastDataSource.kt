package com.dsvag.weather.data.network

import androidx.lifecycle.LiveData
import com.dsvag.weather.data.models.Daily.DailyRequest
import com.dsvag.weather.data.models.Hourly.HourlyRequest

interface ForecastDataSource {
    val downloadHourlyForecast: LiveData<HourlyRequest>
    val downloadDailyForecast: LiveData<DailyRequest>

    private val key: String
        get() = "9d8501a0ac44ca0f9c0b69b07adc0040"

    suspend fun fetchHourlyForecast(
        lat: Double,
        lon: Double,
        exclude: String = "daily",
        units: String = "metric",
        appid: String = key
    )

    suspend fun fetchDailyForecast(
        lat: Double,
        lon: Double,
        exclude: String = "hourly",
        units: String = "metric",
        appid: String = key
    )
}
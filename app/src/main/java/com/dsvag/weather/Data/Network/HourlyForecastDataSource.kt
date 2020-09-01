package com.dsvag.weather.Data.Network

import androidx.lifecycle.LiveData
import com.dsvag.weather.Data.Models.Hourly.HourlyRequest

interface HourlyForecastDataSource {
    val downloadHourlyForecast: LiveData<HourlyRequest>

    suspend fun fetchHourlyForecast(
        lat: Double,
        lon: Double,
        exclude: String = "daily",
        units: String = "metric",
        appid: String = "9d8501a0ac44ca0f9c0b69b07adc0040"
    )
}
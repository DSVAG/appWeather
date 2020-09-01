package com.dsvag.weather.Data.Network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dsvag.weather.Data.Models.Hourly.HourlyRequest
import java.io.IOException

class HourlyForecastDataSourceImpl(private val apiHourlyForecast: ApiHourlyForecast) :
    HourlyForecastDataSource {

    private val _downloadedHourlyForecast = MutableLiveData<HourlyRequest>()
    override val downloadHourlyForecast: LiveData<HourlyRequest>
        get() = _downloadedHourlyForecast

    override suspend fun fetchHourlyForecast(
        lat: Double,
        lon: Double,
        exclude: String,
        units: String,
        appid: String
    ) {
        try {
            val fetchedHourlyForecast = apiHourlyForecast
                .getHourlyForecast(lat, lon, exclude, units, appid)
            _downloadedHourlyForecast.postValue(fetchedHourlyForecast)
        } catch (e: IOException) {
        }
    }
}
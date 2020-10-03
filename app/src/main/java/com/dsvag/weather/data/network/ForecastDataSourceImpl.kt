package com.dsvag.weather.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dsvag.weather.data.models.Daily.DailyRequest
import com.dsvag.weather.data.models.Hourly.HourlyRequest
import java.io.IOException

class ForecastDataSourceImpl(private val apiForecast: ApiForecast) :
    ForecastDataSource {

    private val _downloadedHourlyForecast = MutableLiveData<HourlyRequest>()
    override val downloadHourlyForecast: LiveData<HourlyRequest>
        get() = _downloadedHourlyForecast

    private val _downloadedDailyForecast = MutableLiveData<DailyRequest>()
    override val downloadDailyForecast: LiveData<DailyRequest>
        get() = _downloadedDailyForecast

    override suspend fun fetchHourlyForecast(
        lat: Double, lon: Double, exclude: String, units: String, appid: String
    ) {
        try {
            val fetchedHourlyForecast =
                apiForecast.getHourlyForecast(lat, lon, exclude, units, appid)
            _downloadedHourlyForecast.postValue(fetchedHourlyForecast)
        } catch (e: IOException) {
            Log.e(TAG, "", e)
        }
    }

    override suspend fun fetchDailyForecast(
        lat: Double, lon: Double, exclude: String, units: String, appid: String
    ) {
        try {
            val fetchedDailyForecast = apiForecast.getDailyForecast(lat, lon, exclude, units, appid)
            _downloadedDailyForecast.postValue(fetchedDailyForecast)
        } catch (e: IOException) {
            Log.e(TAG, "", e)
        }
    }

    companion object {
        private const val TAG = "HourlyForecastDataSource"
    }
}
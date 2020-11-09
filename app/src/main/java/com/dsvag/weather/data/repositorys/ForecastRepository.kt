package com.dsvag.weather.data.repositorys

import android.util.Log
import com.dsvag.weather.data.database.ForecastDao
import com.dsvag.weather.data.models.request.Forecast
import com.dsvag.weather.data.network.ApiForecast
import kotlinx.coroutines.flow.Flow

class ForecastRepository(
    private val apiForecast: ApiForecast,
    private val forecastDao: ForecastDao,
) {

    fun getForecastFromDb(): Flow<Forecast> {
        return forecastDao.getForecast()
    }

    suspend fun fetchForecast(
        latitude: Double,
        longitude: Double,
        units: String = "metric",
        exclude: String = "alerts"
    ) {
        return apiForecast.getForecast(latitude, longitude, units, exclude).run {
            if (isSuccessful && body() != null) {
                forecastDao.insert(body()!!)
            } else {
                Log.e(TAG, errorBody().toString())
            }
        }
    }

    companion object {
        private val TAG = ForecastRepository::class.simpleName
    }
}
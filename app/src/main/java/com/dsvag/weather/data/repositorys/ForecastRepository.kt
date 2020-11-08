package com.dsvag.weather.data.repositorys

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.dsvag.weather.data.database.ForecastDao
import com.dsvag.weather.data.models.request.Forecast
import com.dsvag.weather.data.network.ApiForecast

class ForecastRepository(
    private val apiForecast: ApiForecast,
    private val forecastDao: ForecastDao,
    private val preference: SharedPreferences,
) {

    private val editor = preference.edit()

    fun getLocation(): List<Double> {
        val latitude = preference.getFloat(latitudeKey, 91F)
        val longitude = preference.getFloat(longitudeKey, 181F)

        return listOf(latitude, longitude).map { it.toDouble() }
    }

    fun saveLocation(latitude: Double, longitude: Double) {
        editor.putFloat(latitudeKey, latitude.toFloat())
        editor.putFloat(longitudeKey, longitude.toFloat())

        editor.apply()
        editor.commit()
    }

    fun getForecastFromDb(): LiveData<Forecast> {
        return forecastDao.getForecast().asLiveData()
    }

    suspend fun getForecast(latitude: Double, longitude: Double, units: String, exclude: String) {
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

        private const val latitudeKey = "Latitude"
        private const val longitudeKey = "Longitude"
    }
}
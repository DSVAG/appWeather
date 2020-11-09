package com.dsvag.weather.data.di

import android.app.Application
import androidx.room.Room
import com.dsvag.weather.data.database.ForecastDatabase
import com.dsvag.weather.data.network.ApiForecast
import com.dsvag.weather.data.repositorys.ForecastRepository
import com.dsvag.weather.data.repositorys.LocationRepository
import com.google.android.gms.location.LocationServices
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppComponent(application: Application) {

    private val database by lazy {
        Room.databaseBuilder(application, ForecastDatabase::class.java, "database-forecast").build()
    }

    private val forecastDao by lazy { database.forecastDao() }

    private val apiForecast by lazy { retrofit.create(ApiForecast::class.java) }

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(application)
    }

    private val retrofit by lazy {
        Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .build()
    }

    private val requestInterceptor by lazy {

        Interceptor { chain ->

            val url = chain
                .request()
                .url
                .newBuilder()
                .build()

            val request = chain
                .request()
                .newBuilder()
                .url(url)
                .build()

            chain.proceed(request)
        }
    }

    val forecastRepository by lazy {
        ForecastRepository(
            apiForecast,
            forecastDao,
        )
    }

    val locationRepository by lazy {
        LocationRepository(
            fusedLocationClient,
        )
    }
}
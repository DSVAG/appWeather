package com.dsvag.weather.data.di

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.dsvag.weather.data.database.ForecastDatabase
import com.dsvag.weather.data.network.ApiForecast
import com.dsvag.weather.data.repositorys.ForecastRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppComponent(application: Application) {

    private val database by lazy {
        Room.databaseBuilder(application, ForecastDatabase::class.java, "database-forecast").build()
    }

    private val forecastDao by lazy { database.forecastDao() }

    private val apiForecast: ApiForecast by lazy { retrofit.create(ApiForecast::class.java) }

    private val retrofit: Retrofit by lazy {
        Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
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

    private val preference by lazy {
        application.getSharedPreferences("Data", AppCompatActivity.MODE_PRIVATE)
    }

    val repository by lazy { ForecastRepository(apiForecast, forecastDao, preference) }


}
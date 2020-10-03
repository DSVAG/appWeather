package com.dsvag.weather.data.utils

import android.app.Application
import com.dsvag.weather.data.network.ApiForecast
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppComponent(application: Application) {

    val apiService: ApiForecast by lazy { retrofit.create(ApiForecast::class.java) }

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
}
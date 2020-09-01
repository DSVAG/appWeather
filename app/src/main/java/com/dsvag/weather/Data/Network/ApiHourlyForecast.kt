package com.dsvag.weather.Data.Network

import com.dsvag.weather.Data.Models.Hourly.HourlyRequest
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiHourlyForecast {
    @GET("data/2.5/onecall")
    suspend fun getHourlyForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") lang: String = "daily",
        @Query("units") limit: String = "metric",
        @Query("appid") hours: String = "9d8501a0ac44ca0f9c0b69b07adc0040"
    ): HourlyRequest

    companion object {
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor): ApiHourlyForecast {
            val requestInterceptor = Interceptor { chain ->
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
                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient
                .Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit
                .Builder()
                .client(okHttpClient)
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiHourlyForecast::class.java)

        }
    }
}
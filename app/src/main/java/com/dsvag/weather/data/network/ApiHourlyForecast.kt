package com.dsvag.weather.data.network

import com.dsvag.weather.data.models.Daily.DailyRequest
import com.dsvag.weather.data.models.Hourly.HourlyRequest
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiForecast {

    private val key: String
        get() = "9d8501a0ac44ca0f9c0b69b07adc0040"

    @GET("data/2.5/onecall")
    suspend fun getHourlyForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") lang: String = "daily",
        @Query("units") limit: String = "metric",
        @Query("appid") hours: String = key
    ): HourlyRequest

    @GET("data/2.5/onecall")
    suspend fun getDailyForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") lang: String = "hourly",
        @Query("units") limit: String = "metric",
        @Query("appid") hours: String = key
    ): DailyRequest
}
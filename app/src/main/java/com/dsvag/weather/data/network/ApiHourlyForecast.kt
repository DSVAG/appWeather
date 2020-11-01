package com.dsvag.weather.data.network

import com.dsvag.weather.data.models.request.Forecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiForecast {

    private val key: String
        get() = "9d8501a0ac44ca0f9c0b69b07adc0040"

    @GET("data/2.5/onecall")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("exclude") exclude: String,
        @Query("appid") appid: String = key
    ): Response<Forecast>
}
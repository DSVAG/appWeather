package com.dsvag.weather.data.remote

import com.dsvag.weather.models.forecast.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OwmApiService {

    @GET("data/2.5/onecall")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("exclude") exclude: String = "alerts",
    ): ForecastResponse
}
package com.dsvag.weather.Data.Models.Hourly

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class Current(
    val dt: Long,
    val sunrise: Int,
    val sunset: Int,
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("dew_point")
    val dewPoint: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    @SerializedName("wind_deg")
    val windDeg: Int,
    val weather: List<Weather>
)
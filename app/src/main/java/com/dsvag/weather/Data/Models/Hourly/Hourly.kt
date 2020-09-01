package com.dsvag.weather.Data.Models.Hourly

import com.dsvag.weather.Data.Models.Daily.Weather
import com.google.gson.annotations.SerializedName

data class Hourly(
    val dt: Long,
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("dew_point")
    val dewPoint: Double,
    val clouds: Int,
    val visibility: Int,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    @SerializedName("wind_deg")
    val windDeg: Int,
    val weather: List<Weather>,
    val pop: Double,
    val rain: Rain
)
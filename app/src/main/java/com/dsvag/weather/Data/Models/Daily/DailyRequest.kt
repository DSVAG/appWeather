package com.dsvag.weather.Data.Models.Daily

import com.google.gson.annotations.SerializedName

data class DailyRequest(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int,
    val current: Current,
    val daily: List<Daily>
)
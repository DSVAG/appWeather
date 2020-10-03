package com.dsvag.weather.data.models.Hourly

import com.google.gson.annotations.SerializedName

data class HourlyRequest(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int,
    val current: Current,
    val hourly: List<Hourly>
)
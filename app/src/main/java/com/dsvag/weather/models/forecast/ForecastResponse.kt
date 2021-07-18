package com.dsvag.weather.models.forecast


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForecastResponse(
    @Json(name = "current")
    val current: Current,

    @Json(name = "daily")
    val daily: List<Daily>,

    @Json(name = "hourly")
    val hourly: List<Hourly>,

    @Json(name = "lat")
    val lat: Double,

    @Json(name = "lon")
    val lon: Double,

    @Json(name = "minutely")
    val minutely: List<Minutely>,

    @Json(name = "timezone")
    val timezone: String,

    @Json(name = "timezone_offset")
    val timezoneOffset: Int
)
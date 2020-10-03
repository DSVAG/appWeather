package com.dsvag.weather.data.models.Hourly

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
package com.dsvag.weather.Data.Models.Daily

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
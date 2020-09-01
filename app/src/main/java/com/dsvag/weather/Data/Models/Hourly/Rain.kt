package com.dsvag.weather.Data.Models.Hourly


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val h: Double
)
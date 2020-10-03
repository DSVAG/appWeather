package com.dsvag.weather.data.models.Hourly

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val h: Double
)
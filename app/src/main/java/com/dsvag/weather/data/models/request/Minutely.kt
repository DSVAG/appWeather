package com.dsvag.weather.data.models.request

import com.google.gson.annotations.SerializedName

data class Minutely(
    @SerializedName("dt")
    val dt: Long,
    @SerializedName("precipitation")
    val precipitation: Double
)
package com.dsvag.weather.data.models.request

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "forecast")
data class Forecast(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int,

    @SerializedName("current")
    val current: Current,

    @SerializedName("minutely")
    val minutely: List<Minutely>,

    @SerializedName("hourly")
    val hourly: List<Hourly>,

    @SerializedName("daily")
    val daily: List<Daily>,

    @PrimaryKey(autoGenerate = false)
    val id: Int = 0
)
package com.dsvag.weather.data.utils

import androidx.room.TypeConverter
import com.dsvag.weather.data.models.request.Current
import com.dsvag.weather.data.models.request.Daily
import com.dsvag.weather.data.models.request.Hourly
import com.dsvag.weather.data.models.request.Minutely
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromCurrent(current: Current): String = Gson().toJson(current)

    @TypeConverter
    fun toCurrent(string: String): Current {
        return Gson().fromJson(string, Current::class.java)
    }


    @TypeConverter
    fun fromMinutely(minutely: List<Minutely>): String = Gson().toJson(minutely)

    @TypeConverter
    fun toMinutely(string: String): List<Minutely> {
        val type = object : TypeToken<List<Minutely>>() {}.type
        return Gson().fromJson(string, type)
    }


    @TypeConverter
    fun fromHourly(hourly: List<Hourly>): String = Gson().toJson(hourly)

    @TypeConverter
    fun toHourly(string: String): List<Hourly> {
        val type = object : TypeToken<List<Hourly>>() {}.type
        return Gson().fromJson(string, type)
    }


    @TypeConverter
    fun fromDaily(daily: List<Daily>): String = Gson().toJson(daily)

    @TypeConverter
    fun toDaily(string: String): List<Daily> {
        val type = object : TypeToken<List<Daily>>() {}.type
        return Gson().fromJson(string, type)
    }
}
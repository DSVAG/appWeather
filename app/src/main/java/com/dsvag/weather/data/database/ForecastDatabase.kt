package com.dsvag.weather.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dsvag.weather.data.models.request.Forecast
import com.dsvag.weather.data.utils.Converters

@Database(entities = [Forecast::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
}
package com.dsvag.weather.data.database

import androidx.room.*
import com.dsvag.weather.data.models.request.Forecast
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(forecast: Forecast)

    @Delete
    fun delete(forecast: Forecast)

    @Query("SELECT * FROM forecast")
    fun getForecast(): Flow<Forecast>
}
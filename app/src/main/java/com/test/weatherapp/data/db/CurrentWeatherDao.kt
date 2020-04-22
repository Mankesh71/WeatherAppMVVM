package com.test.weatherapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.weatherapp.data.db.entity.CURRENT_WEATHER_ID
import com.test.weatherapp.data.db.entity.CurrentWeatherData
import com.test.weatherapp.data.db.unitlocalized.current.CurrentWeatherEntry

@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(currentWeatherData: CurrentWeatherData)

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getCurrentWeatherEntry(): LiveData<CurrentWeatherEntry>
}
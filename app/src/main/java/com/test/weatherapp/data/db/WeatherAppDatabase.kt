package com.test.weatherapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.weatherapp.data.db.entity.CurrentWeatherData
import com.test.weatherapp.data.db.entity.WeatherLocation

@Database(
    entities = [CurrentWeatherData::class, WeatherLocation::class],
    version = 1
)
abstract class WeatherAppDatabase: RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun weatherLocationDao(): WeatherLocationDao

    companion object {
        @Volatile  private var instance: WeatherAppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext, WeatherAppDatabase::class.java, "forecast.db").build()
    }
}
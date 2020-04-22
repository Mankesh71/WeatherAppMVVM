package com.test.weatherapp.data.repository

import androidx.lifecycle.LiveData
import com.test.weatherapp.data.db.entity.WeatherLocation
import com.test.weatherapp.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry

interface WeatherRepository {
    suspend fun getCurrentWeather(isMetric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<out WeatherLocation>
}
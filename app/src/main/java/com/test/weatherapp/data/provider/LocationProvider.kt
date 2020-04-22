package com.test.weatherapp.data.provider

import com.test.weatherapp.data.db.entity.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation) : Boolean
    suspend fun getLocationFromPreference() : String
}
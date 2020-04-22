package com.test.weatherapp.data.network.response

import com.test.weatherapp.data.db.entity.CurrentWeatherData
import com.test.weatherapp.data.db.entity.RequestData
import com.test.weatherapp.data.db.entity.WeatherLocation

data class CurrentWeatherResponse(
    val current: CurrentWeatherData,
    val location: WeatherLocation,
    val request: RequestData
)
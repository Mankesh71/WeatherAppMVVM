package com.test.weatherapp.data.network

import androidx.lifecycle.LiveData
import com.test.weatherapp.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(location: String, language: String)
}
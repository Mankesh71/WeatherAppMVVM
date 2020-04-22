package com.test.weatherapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.weatherapp.data.internal.NoConnectivityException
import com.test.weatherapp.data.network.response.CurrentWeatherResponse

class WeatherNetworkDataSourceImpl(private val apiService: WeatherApiService) : WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, language: String) {
        try {
            val fetchCurrentWeather = apiService
                .getCurrentWeather(location = location)
                .await()
                _downloadedCurrentWeather.postValue(fetchCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection")
        }
    }
}
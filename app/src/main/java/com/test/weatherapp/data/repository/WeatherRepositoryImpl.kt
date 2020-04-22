package com.test.weatherapp.data.repository

import androidx.lifecycle.LiveData
import com.test.weatherapp.data.db.CurrentWeatherDao
import com.test.weatherapp.data.db.WeatherLocationDao
import com.test.weatherapp.data.db.entity.WeatherLocation
import com.test.weatherapp.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.test.weatherapp.data.network.WeatherNetworkDataSource
import com.test.weatherapp.data.network.response.CurrentWeatherResponse
import com.test.weatherapp.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*

class WeatherRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val locationProvider: LocationProvider,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : WeatherRepository {
    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(weatherResponse = newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(isMetric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext currentWeatherDao.getCurrentWeatherEntry()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<out WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getWeatherLocation()
        }
    }

    private fun persistFetchedCurrentWeather(weatherResponse: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(currentWeatherData = weatherResponse.current)
            weatherLocationDao.upsert(weatherLocation = weatherResponse.location)
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getWeatherLocation().value
        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation = lastWeatherLocation)) {
            fetchCurrentWeatherData()
            return
        }
        if (isFetchCurrentNeeded(lastFetchTime = lastWeatherLocation.zonedDateTime))
            fetchCurrentWeatherData()
    }

    private suspend fun fetchCurrentWeatherData() {
        weatherNetworkDataSource.fetchCurrentWeather(
            location = locationProvider.getLocationFromPreference(),
            language = Locale.getDefault().language
        )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinute = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinute)
    }
}
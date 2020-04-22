package com.test.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModel
import com.test.weatherapp.data.internal.UnitSystem
import com.test.weatherapp.data.internal.lazyDeferred
import com.test.weatherapp.data.provider.UnitProvider
import com.test.weatherapp.data.repository.WeatherRepository

class CurrentWeatherViewModel(
    private val repository: WeatherRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    private val unitSystem = unitProvider.getUnitSystem()

    private val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC
    val weather by lazyDeferred {
        repository.getCurrentWeather(isMetric = isMetric)
    }

    val weatherLocation by lazyDeferred {
        repository.getWeatherLocation()
    }
}

package com.test.weatherapp.ui.weather.current

import com.test.weatherapp.data.internal.lazyDeferred
import com.test.weatherapp.data.provider.UnitProvider
import com.test.weatherapp.data.repository.WeatherRepository
import com.test.weatherapp.ui.base.WeatherViewModel

class CurrentWeatherViewModel(
    private val repository: WeatherRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(repository = repository, unitProvider = unitProvider) {

    val weather by lazyDeferred {
        repository.getCurrentWeather(isMetric = super.isMetricUnit)
    }
}

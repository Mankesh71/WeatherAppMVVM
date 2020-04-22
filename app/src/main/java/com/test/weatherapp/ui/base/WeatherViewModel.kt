package com.test.weatherapp.ui.base

import androidx.lifecycle.ViewModel
import com.test.weatherapp.data.internal.UnitSystem
import com.test.weatherapp.data.internal.lazyDeferred
import com.test.weatherapp.data.provider.UnitProvider
import com.test.weatherapp.data.repository.WeatherRepository

abstract class WeatherViewModel(
    private val repository: WeatherRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetricUnit: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weatherLocation by lazyDeferred {
        repository.getWeatherLocation()
    }

}
package com.test.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.weatherapp.data.provider.UnitProvider
import com.test.weatherapp.data.repository.WeatherRepository

class CurrentWeatherViewModelProvider(
    private val weatherRepository: WeatherRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(repository = weatherRepository, unitProvider = unitProvider) as T
    }
}
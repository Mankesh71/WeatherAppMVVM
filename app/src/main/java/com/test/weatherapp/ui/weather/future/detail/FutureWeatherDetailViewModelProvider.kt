package com.test.weatherapp.ui.weather.future.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.weatherapp.data.provider.UnitProvider
import com.test.weatherapp.data.repository.WeatherRepository

//
// Created by Mankesh71 on 4/23/2020.
// Copyright (c) 2020 Mankesh71. All rights reserved.
//
class FutureWeatherDetailViewModelProvider(
    private val weatherRepository: WeatherRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureWeatherDetailViewModel(repository = weatherRepository, unitProvider = unitProvider) as T
    }
}
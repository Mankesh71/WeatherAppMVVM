package com.test.weatherapp.data.provider

import com.test.weatherapp.data.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}
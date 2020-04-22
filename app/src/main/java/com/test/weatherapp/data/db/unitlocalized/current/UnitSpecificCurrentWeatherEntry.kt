package com.test.weatherapp.data.db.unitlocalized.current

interface UnitSpecificCurrentWeatherEntry {
    val temperature: Double
    val windSpeed: Double
    val windDirection: String?
    val observationTime: String?
    val weatherDescription: String?
    val weatherIcons: String?
    val precipitationVolume: Double
    val feelsLikeTemperature: Double
    val visibilityDistance: Double
    val uvIndex: Double
    val pressure: Double
    val humidity: Double
}
package com.test.weatherapp.data.db.unitlocalized.current

import androidx.room.ColumnInfo

class CurrentWeatherEntry(
    @ColumnInfo(name = "temperature")
    override val temperature: Double,
    @ColumnInfo(name = "windSpeed")
    override val windSpeed: Double,
    @ColumnInfo(name = "windDirection")
    override val windDirection: String?,
    @ColumnInfo(name = "precip")
    override val precipitationVolume: Double,
    @ColumnInfo(name = "feelsLike")
    override val feelsLikeTemperature: Double,
    @ColumnInfo(name = "visibility")
    override val visibilityDistance: Double,
    @ColumnInfo(name = "uvIndex")
    override val uvIndex: Double,
    @ColumnInfo(name = "pressure")
    override val pressure: Double,
    @ColumnInfo(name = "humidity")
    override val humidity: Double
) : UnitSpecificCurrentWeatherEntry
package com.test.weatherapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class CurrentWeatherData(
    @SerializedName("feelslike")
    val feelsLike: Double,
    val humidity: Double,
    @SerializedName("is_day")
    val isDay: String?,
    @SerializedName("observation_time")
    val observationTime: String?,
    val pressure: Double,
    val precip: Double,
    val temperature: Double,
    @SerializedName("uv_index")
    val uvIndex: Double,
    val visibility: Double,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("wind_dir")
    val windDirection: String?,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    @SerializedName("weather_descriptions")
    val weatherDescription: List<String>,
    @SerializedName("weather_icons")
    val weatherIcons: List<String>
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}

object ListTypeConverters {

    @TypeConverter
    @JvmStatic
    fun listToJson(value: List<String>?) = Gson().toJson(value)!!

    @TypeConverter
    @JvmStatic
    fun jsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}
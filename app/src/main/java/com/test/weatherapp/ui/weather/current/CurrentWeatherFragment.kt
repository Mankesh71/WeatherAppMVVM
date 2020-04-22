package com.test.weatherapp.ui.weather.current

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.test.weatherapp.R
import com.test.weatherapp.data.db.entity.ListTypeConverters
import com.test.weatherapp.data.internal.glide.GlideApp
import com.test.weatherapp.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

@SuppressLint("SetTextI18n")
class CurrentWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val currentWeatherViewModelProvider by instance<CurrentWeatherViewModelProvider>()
    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            currentWeatherViewModelProvider
        ).get(CurrentWeatherViewModel::class.java)
        setupUi()
    }

    private fun setupUi() = launch {
        val currentWeather = viewModel.weather.await()
        currentWeather.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            group_loading.visibility = View.GONE
            updateTemperature(temperature = it.temperature, feelsLike = it.feelsLikeTemperature)
            updatePrecipitation(precipitation = it.precipitationVolume, humidity = it.humidity)
            if (it.windDirection != null) {
                updateWind(windDirection = it.windDirection!!, windSpeed = it.windSpeed)
            } else {
                updateWind(windDirection = "", windSpeed = it.windSpeed)
            }
            updateVisibility(visibility = it.visibilityDistance, uvIndex = it.uvIndex)
            if (it.weatherDescription != null)
                updateCondition(condition = ListTypeConverters.jsonToList(it.weatherDescription!!)[0])
            if (it.weatherIcons != null)
                updateWeatherIcon(iconUrl = ListTypeConverters.jsonToList(it.weatherIcons!!)[0])
        })

        val weatherLocation = viewModel.weatherLocation.await()
        weatherLocation.observe(viewLifecycleOwner, Observer { location ->
            if (location == null) return@Observer
            updateLocation(location.name)
            updateDateToToday()
        })
    }

    private fun updateLocation(location: String) {
        (activity as AppCompatActivity).supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as AppCompatActivity).supportActionBar?.subtitle =
            resources.getString(R.string.today)
    }

    private fun updateTemperature(temperature: Double, feelsLike: Double) {
        val unit = "°C"
        textView_temperature.text = "$temperature $unit"
        textView_feels_like_temperature.text = "Feels like $feelsLike $unit"
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitation: Double, humidity: Double) {
        textView_precipitation.text = "Precipitation: - $precipitation mm\nHumidity: - $humidity%"
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        textView_wind.text = "Wind direction: - $windDirection\nwind speed: - $windSpeed kmph"
    }

    private fun updateVisibility(visibility: Double, uvIndex: Double) {
        textView_visibility.text = "Visibility: - $visibility km\nUV Index $uvIndex"
    }

    private fun updateWeatherIcon(iconUrl: String) {
        var url = iconUrl
        if (!url.contains("http"))
            url = "http:$url"
        GlideApp.with(this)
            .load(url)
            .into(imageView_condition_icon)
    }

}

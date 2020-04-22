package com.test.weatherapp.ui.weather.future.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.test.weatherapp.R
import com.test.weatherapp.data.db.entity.ListTypeConverters
import com.test.weatherapp.data.internal.glide.GlideApp
import com.test.weatherapp.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.future_weather_detail_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

@SuppressLint("SetTextI18n")
class FutureWeatherDetailFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val futureWeatherDetailViewModelProvider by instance<FutureWeatherDetailViewModelProvider>()
    private lateinit var viewModel: FutureWeatherDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_weather_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, futureWeatherDetailViewModelProvider).get(FutureWeatherDetailViewModel::class.java)
        val safeArgs = arguments?.let { FutureWeatherDetailFragmentArgs.fromBundle(it) }
        Toast.makeText(context, safeArgs?.date, Toast.LENGTH_SHORT).show()
//        val date = LocalDateConverter.stringToDate(safeArgs?.date) ?: throw DateNotFoundException()

        bindUI()
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val futureWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(viewLifecycleOwner, Observer { location ->
            if (location == null) return@Observer
            updateLocation(location.name)
        })

        futureWeather.observe(viewLifecycleOwner, Observer { weatherEntry ->
            if (weatherEntry == null) return@Observer

            updateDate(weatherEntry.observationTime!!)
            updateTemperatures(weatherEntry.temperature,
                weatherEntry.feelsLikeTemperature, weatherEntry.temperature)
            updateCondition(ListTypeConverters.jsonToList(weatherEntry.weatherDescription!!)[0])
            updatePrecipitation(weatherEntry.precipitationVolume)
            updateWindSpeed(weatherEntry.windSpeed)
            updateVisibility(weatherEntry.visibilityDistance)
            updateUv(weatherEntry.uvIndex)

            var url = ListTypeConverters.jsonToList(weatherEntry.weatherIcons!!)[0]
            if (!url.contains("http"))
                url = "http:$url"
            GlideApp.with(this@FutureWeatherDetailFragment)
                .load(url)
                .into(imageView_condition_icon)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetricUnit) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDate(date: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }

    private fun updateTemperatures(temperature: Double, min: Double, max: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        textView_temperature.text = "$temperature$unitAbbreviation"
        textView_min_max_temperature.text = "Min: $min$unitAbbreviation, Max: $max$unitAbbreviation"
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        textView_precipitation.text = "Precipitation: $precipitationVolume $unitAbbreviation"
    }

    private fun updateWindSpeed(windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        textView_wind.text = "Wind speed: $windSpeed $unitAbbreviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi.")
        textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }

    private fun updateUv(uv: Double) {
        textView_uv.text = "UV: $uv"
    }

}

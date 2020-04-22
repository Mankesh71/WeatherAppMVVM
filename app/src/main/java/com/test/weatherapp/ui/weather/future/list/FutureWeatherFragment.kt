package com.test.weatherapp.ui.weather.future.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.weatherapp.R
import com.test.weatherapp.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.test.weatherapp.ui.base.ScopedFragment
import com.test.weatherapp.ui.weather.future.list.adapter.FutureWeatherItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.future_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class FutureWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val futureWeatherViewModelProvider by instance<FutureWeatherViewModelProvider>()
    private lateinit var viewModel: FutureWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, futureWeatherViewModelProvider).get(FutureWeatherViewModel::class.java)
        setupUi()
    }

    private fun setupUi() = launch {
        val currentWeather = viewModel.weather.await()
        currentWeather.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            group_loading.visibility = View.GONE
            val list = mutableListOf<UnitSpecificCurrentWeatherEntry>()
            for (i in 0..10) {
                list.add(it)
            }
            initRecycleView(list.toFutureWeatherItems())
        })

        val weatherLocation = viewModel.weatherLocation.await()
        weatherLocation.observe(viewLifecycleOwner, Observer { location ->
            if (location == null) return@Observer
            updateLocation(location.name)
            updateDateToNextWeek()
        })
    }

    private fun updateLocation(location: String) {
        (activity as AppCompatActivity).supportActionBar?.title = location
    }

    private fun updateDateToNextWeek() {
        (activity as AppCompatActivity).supportActionBar?.subtitle =
            resources.getString(R.string.next_week)
    }

    private fun List<UnitSpecificCurrentWeatherEntry>.toFutureWeatherItems(): List<FutureWeatherItem> {
        return this.map {
            FutureWeatherItem(
                it
            )
        }
    }

    private fun initRecycleView(items: List<FutureWeatherItem>) {
        val listAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FutureWeatherFragment.context)
            adapter = listAdapter
        }
        listAdapter.setOnItemClickListener { item, view ->
            (item as? FutureWeatherItem)?.let {
                showWeatherDetail(view)
            }
        }
    }

    private fun showWeatherDetail(view: View) {
        val actionDetail = FutureWeatherFragmentDirections.actionDetail("")
        Navigation.findNavController(view).navigate(actionDetail)
    }

}

package com.test.weatherapp.application

import android.app.Application
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import com.test.weatherapp.data.db.WeatherAppDatabase
import com.test.weatherapp.data.network.*
import com.test.weatherapp.data.provider.LocationProvider
import com.test.weatherapp.data.provider.LocationProviderImpl
import com.test.weatherapp.data.provider.UnitProvider
import com.test.weatherapp.data.provider.UnitProviderImpl
import com.test.weatherapp.data.repository.WeatherRepository
import com.test.weatherapp.data.repository.WeatherRepositoryImpl
import com.test.weatherapp.ui.weather.current.CurrentWeatherViewModelProvider
import com.test.weatherapp.ui.weather.future.detail.FutureWeatherDetailViewModelProvider
import com.test.weatherapp.ui.weather.future.list.FutureWeatherViewModelProvider
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class WeatherAppApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@WeatherAppApplication))

        bind() from singleton { WeatherAppDatabase(instance()) }
        bind() from singleton { instance<WeatherAppDatabase>().currentWeatherDao() }
        bind() from singleton { instance<WeatherAppDatabase>().weatherLocationDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<WeatherRepository>() with singleton {
            WeatherRepositoryImpl(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { CurrentWeatherViewModelProvider(instance(), instance()) }
        bind() from provider { FutureWeatherViewModelProvider(instance(), instance()) }
        bind() from provider { FutureWeatherDetailViewModelProvider(instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}
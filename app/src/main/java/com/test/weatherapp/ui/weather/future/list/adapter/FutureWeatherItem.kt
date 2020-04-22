package com.test.weatherapp.ui.weather.future.list.adapter

import android.annotation.SuppressLint
import com.test.weatherapp.R
import com.test.weatherapp.data.db.entity.ListTypeConverters
import com.test.weatherapp.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.test.weatherapp.data.internal.glide.GlideApp
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.layout_future_weather_details.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

//
// Created by Mankesh71 on 4/23/2020.
// Copyright (c) 2020 Mankesh71. All rights reserved.
//
@SuppressLint("SetTextI18n")
class FutureWeatherItem(private val weatherEntry: UnitSpecificCurrentWeatherEntry) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            updateTemperature()
            updateDate()
            updateImage()
            updateCondition()
        }
    }

    private fun ViewHolder.updateTemperature() {
        val unit = "°C"
        textView_temperature.text = "${weatherEntry.temperature} $unit"
    }

    override fun getLayout(): Int {
        return R.layout.layout_future_weather_details
    }

    private fun ViewHolder.updateDate() {
        if (weatherEntry.observationTime != null) {
            val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            textView_date.text = weatherEntry.observationTime!!.format(dateFormatter)
        }
    }

    private fun ViewHolder.updateImage() {
        var url = ListTypeConverters.jsonToList(weatherEntry.weatherIcons!!)[0]
        if (!url.contains("http"))
            url = "http:$url"
        GlideApp.with(this.containerView)
            .load(url)
            .into(imageView_condition_icon)
    }

    private fun ViewHolder.updateCondition() {
        textView_condition.text =
            ListTypeConverters.jsonToList(weatherEntry.weatherDescription!!)[0]
    }
}
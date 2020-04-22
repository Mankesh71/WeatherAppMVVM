package com.test.weatherapp.data.db.entity

data class RequestData(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
)
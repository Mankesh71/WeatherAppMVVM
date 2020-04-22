package com.test.weatherapp.data.internal

import java.io.IOException

class NoConnectivityException: IOException()

class LocationPermissionNotGrantedException: Exception()

class DateNotFoundException: Exception()
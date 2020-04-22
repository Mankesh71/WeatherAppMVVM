package com.test.weatherapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.test.weatherapp.data.internal.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ConnectivityInterceptorImpl(context: Context) : ConnectivityInterceptor {
    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            return if (isNetworkAvailable())
                handleApiResponse(chain.proceed(chain.request()))
            else
                throw NoConnectivityException()
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> throw NoConnectivityException()
                is SocketException -> throw NoConnectivityException()
                is UnknownHostException -> throw NoConnectivityException()
                else -> throw NoConnectivityException()
            }
        }
    }

    /**
     * Process API response and return appropriate response back to caller.
     *
     * Return: response if status is 200 otherwise throwing Exception.
     */
    private fun handleApiResponse(response: Response): Response {
        when (response.code()) {
            in HttpURLConnection.HTTP_OK..HttpURLConnection.HTTP_USE_PROXY -> return response
            HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                showInternalError(response = response)
            }
            HttpURLConnection.HTTP_NOT_FOUND -> {
                showInternalError(response = response)
            }
            HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> {
                showInternalError(response = response)
            }
            HttpURLConnection.HTTP_UNAUTHORIZED -> {

            }
            HttpURLConnection.HTTP_BAD_REQUEST -> {

            }
            else ->
                showInternalError(response = response)
        }
        return response
    }

    private fun showInternalError(response: Response): Response {

        return response
    }

    @Suppress("DEPRECATION")
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager?.run {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                    return when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            connectivityManager?.run {
                connectivityManager.activeNetworkInfo?.run {
                    return when (type) {
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_WIFI -> true
                        else -> false
                    }
                }
            }
        }
        return false
    }
}
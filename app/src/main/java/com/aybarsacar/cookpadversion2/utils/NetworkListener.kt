package com.aybarsacar.cookpadversion2.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow


class NetworkListener : ConnectivityManager.NetworkCallback() {

  private val _isNetworkAvailable = MutableStateFlow(false)

  fun checkNetworkAvailability(context: Context): MutableStateFlow<Boolean> {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    connectivityManager.registerDefaultNetworkCallback(this)

    var isConnected = false

    connectivityManager.allNetworks.forEach {
      val networkCapability = connectivityManager.getNetworkCapabilities(it)

      networkCapability?.let {
        if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
          isConnected = true
          return@forEach
        }
      }
    }

    _isNetworkAvailable.value = isConnected

    return _isNetworkAvailable
  }


  override fun onAvailable(network: Network) {
    _isNetworkAvailable.value = true
  }

  override fun onLost(network: Network) {
    _isNetworkAvailable.value = false
  }
}
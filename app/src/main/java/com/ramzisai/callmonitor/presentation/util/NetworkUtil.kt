package com.ramzisai.callmonitor.presentation.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.NetworkCapabilities
import java.net.Inet4Address

object NetworkUtil {
    fun getWifiIpAddress(context: Context): String? {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return null
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return null

        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            val linkProperties: LinkProperties = connectivityManager.getLinkProperties(network) ?: return null
            for (address in linkProperties.linkAddresses) {
                val ip = address.address
                if (ip is Inet4Address) {
                    return ip.hostAddress
                }
            }
        }
        return null
    }
}
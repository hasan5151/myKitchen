package com.yaros.kitchen.utils

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import java.net.NetworkInterface

class IpAddress (val context : Context){
    fun fromWifi() : String{
        val wm: WifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var s  = android.text.format.Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress())
        if (s!!.contentEquals("0.0.0.0")){
            s=  fromMobile()
        }
        return s
    }

    fun fromMobile(): String? {
        try {
            val en =
                NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val networkinterface = en.nextElement()
                val enumIpAddr =
                    networkinterface.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        return inetAddress.hostAddress.toString()
                    }
                }
            }
        } catch (ex: Exception) {
            Log.e("Current IP", ex.toString())
        }
        return null
    }
}
package cn.dbyl.carclient.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager


/**
 * Create by young
 **/

object NetWorkUtils {
    /**
     * check network
     *
     * @param paramContext
     * @return
     */
    fun checkEnable(paramContext: Context): Boolean {
        val i = false
        val localNetworkInfo = (paramContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return localNetworkInfo != null && localNetworkInfo.isAvailable
    }

    /**
     * Get IP
     *
     * @param ipInt
     * @return
     */
    fun int2ip(ipInt: Int): String? {
        val sb = StringBuilder()
        sb.append(ipInt and 0xFF).append(".")
        sb.append(ipInt shr 8 and 0xFF).append(".")
        sb.append(ipInt shr 16 and 0xFF).append(".")
        sb.append(ipInt shr 24 and 0xFF)
        return sb.toString()
    }

    /**
     * Get local IP
     *
     * @param context
     * @return
     */
    fun getLocalIpAddress(context: Context): String? {
        return try {
            val wifiManager = context
                .getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val i = wifiInfo.ipAddress
            int2ip(i)
        } catch (ex: Exception) {
            " Unable to get IP , please try again" + ex.message
        }
    }
}
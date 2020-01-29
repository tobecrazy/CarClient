package cn.dbyl.carclient.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import cn.dbyl.carclient.data.Constant
import cn.dbyl.carclient.data.IpMap

/**
 * Create by Young 01/11/2020
 **/
class SharedPreferencesUtils(var mContext: Context) {

    @Volatile
    private var instance: SharedPreferencesUtils? = null

    fun getInstance(): SharedPreferencesUtils {
        if (null == instance) {
            synchronized(SharedPreferencesUtils::class.java)
            {
                if (null == instance) {
                    instance = SharedPreferencesUtils(mContext)
                }
            }
        }
        return instance!!
    }




    private fun sharedPreferences(): SharedPreferences {
        return mContext.getSharedPreferences(Constant.DATA, MODE_PRIVATE)
    }

    fun getEditor(): SharedPreferences.Editor {
        return sharedPreferences().edit()
    }

    fun saveIpInfo(maps: IpMap) {
        Log.d("YoungTest","ip is ${maps.ip} Port is ${maps.port}")
        getEditor().putString(Constant.IP, maps.ip)
        getEditor().putInt(Constant.MPORT, maps.port)
        getEditor().commit()
    }

    fun getIpInfo(): IpMap {
        val ip = sharedPreferences().getString(Constant.IP, "192.168.1.1")
        val port = sharedPreferences().getInt(Constant.MPORT, Constant.PORT)
        Log.d("YoungTest","ip is $ip Port is $port")
        return IpMap(ip!!, port)
    }
}
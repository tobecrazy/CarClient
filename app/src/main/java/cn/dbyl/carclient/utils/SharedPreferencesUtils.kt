package cn.dbyl.carclient.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

/**
 * Create by Young 01/11/2020
 **/
class SharedPreferencesUtils constructor(var context: Context) {
    fun getInstance():SharedPreferences{
        return context.getSharedPreferences("data",MODE_PRIVATE)
    }

    fun getEditor():SharedPreferences.Editor{
        return getInstance().edit()
    }
}
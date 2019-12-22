package cn.dbyl.carclient.service

import android.content.Context
import android.os.Binder
import cn.dbyl.carclient.utils.NetWorkUtils

/**
 * Create by Young on 12/21/2019
 **/
class CarRemoterBinder : Binder() {

    fun isNetworkEanble(context: Context): Boolean {
        return NetWorkUtils.checkEnable(context)
    }
}
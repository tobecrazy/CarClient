package cn.dbyl.carclient

import android.app.Application
import android.util.Log
import cn.dbyl.carclient.database.DataManager
import cn.dbyl.carclient.database.MyObjectBox
import cn.dbyl.carclient.utils.NetWorkUtils
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser


/**
 * Create by Young on 11/24/2019
 **/
class ClientApp : Application() {
     lateinit var boxStore: BoxStore
    override fun onCreate() {
        super.onCreate()
        initialGetCurrentIp()
        boxStore=getObjectBox()
        startDatabaseBrowser()
    }

    private fun startDatabaseBrowser() {
        if (BuildConfig.DEBUG) {
            var stated: Boolean = AndroidObjectBrowser(boxStore).start(this)
            Log.d(TAG, "is started = $stated")
        }
        DataManager().getInstance()?.init(this)
    }

    private fun getObjectBox(): BoxStore {
        Log.d(TAG, "getObjectBox() ===> ")
        return MyObjectBox.builder().androidContext(this).build()
    }

    private fun initialGetCurrentIp(): String? {
        val ip = NetWorkUtils.getLocalIpAddress(this)
        if (ip != null) {

        }
        return ip
    }

    companion object {
        val TAG = ClientApp::class.java.canonicalName
    }
}
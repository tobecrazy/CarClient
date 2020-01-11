package cn.dbyl.carclient

import android.app.Application
import android.util.Log
import cn.dbyl.carclient.database.DataManager
import cn.dbyl.carclient.database.MyObjectBox
import cn.dbyl.carclient.utils.NetWorkUtils
import com.clj.fastble.BleManager
import com.clj.fastble.scan.BleScanRuleConfig
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser


/**
 * Create by i321533 (young.liu@sap.com) on 11/24/2019
 **/
class ClientApp : Application() {
     lateinit var boxStore: BoxStore
    override fun onCreate() {
        super.onCreate()
        //initial blue tooth
        initialBlueTooth()
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

    private fun initialBlueTooth() {
        BleManager.getInstance().init(this)
        if (isSupportBlueTooth()) {
            BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000).operateTimeout = 5000

            val scanRuleConfig = BleScanRuleConfig.Builder()
                .setScanTimeOut(10000)
                .build()
            BleManager.getInstance().initScanRule(scanRuleConfig)
        }
    }

    companion object {
        val TAG = ClientApp::class.java.canonicalName
        fun isSupportBlueTooth(): Boolean {
            return BleManager.getInstance().isSupportBle
        }
    }
}
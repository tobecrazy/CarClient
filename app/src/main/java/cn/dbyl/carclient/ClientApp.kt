package cn.dbyl.carclient

import android.app.Application
import android.widget.Toast
import cn.dbyl.carclient.utils.NetWorkUtils
import com.clj.fastble.BleManager
import com.clj.fastble.scan.BleScanRuleConfig
import com.google.android.material.snackbar.Snackbar


/**
 * Create by Young  on 11/24/2019
 **/
class ClientApp : Application() {
    override fun onCreate() {
        super.onCreate()
        //initial blue tooth
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
        val ip = NetWorkUtils.getLocalIpAddress(this)
        if (ip != null) {
            //TODO
        }
    }

    companion object {
        fun isSupportBlueTooth(): Boolean {
            return BleManager.getInstance().isSupportBle
        }
    }
}
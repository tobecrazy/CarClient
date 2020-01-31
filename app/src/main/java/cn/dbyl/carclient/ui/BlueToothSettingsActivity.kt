package cn.dbyl.carclient.ui

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import cn.dbyl.carclient.R
import com.clj.fastble.BleManager
import com.clj.fastble.scan.BleScanRuleConfig
import kotlinx.android.synthetic.main.blue_tooth_settings_activity.*
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice


class BlueToothSettingsActivity : AppCompatActivity() {

    private val isAutoConnect=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blue_tooth_settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initialBlueTooth()
        initialView()
    }

    private fun initialView() {
        blue_tooth_scan.setOnClickListener {
            BleManager.getInstance().cancelScan()
            BleManager.getInstance().scan(object:BleScanCallback(){
                override fun onScanFinished(scanResultList: MutableList<BleDevice>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onScanStarted(success: Boolean) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onScanning(bleDevice: BleDevice?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })

              }
    }

    private fun initialBlueTooth() {
        BleManager.getInstance().init(application)
        if (BleManager.getInstance().isSupportBle) {
            BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000).operateTimeout = 5000

            val scanRuleConfig = BleScanRuleConfig.Builder()
                .setScanTimeOut(10000)
                .setAutoConnect(isAutoConnect)
                .build()
            BleManager.getInstance().initScanRule(scanRuleConfig)
            if (!BleManager.getInstance().isBlueEnable)
            {
                try {
                    BleManager.getInstance().enableBluetooth()
                }catch (e:Exception)
                {
                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(intent, 0x01)
                }
            }
        }else
        {
//            throw ExceptionInInitializerError("Error, no blue tooth devices")
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
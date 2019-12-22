package cn.dbyl.carclient.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import cn.dbyl.carclient.utils.NetWorkUtils


/**
 * Create by Young on 12/17/2019
 **/
class CarRemoteService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        val binder=CarRemoterBinder()
        return binder
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        var message: String = "Service is started"
        if (intent?.getBooleanExtra("Car", false) == true) {
            message = "No listener"
        }
        val isNetWorkEnable=NetWorkUtils.checkEnable(this)
        Toast.makeText(this, "$message ===> $isNetWorkEnable", Toast.LENGTH_LONG).show()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        val message: String = "Service is stopped"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
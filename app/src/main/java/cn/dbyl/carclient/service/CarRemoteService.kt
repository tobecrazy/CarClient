package cn.dbyl.carclient.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast


/**
 * Create by Young on 12/17/2019
 **/
class CarRemoteService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "服务已经停止", Toast.LENGTH_LONG).show()
    }
}
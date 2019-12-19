package cn.dbyl.carclient.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View.OnClickListener
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import cn.dbyl.carclient.R
import cn.dbyl.carclient.databinding.ActivityMainBindingImpl
import cn.dbyl.carclient.service.CarRemoteService
import cn.dbyl.carclient.utils.HttpUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.util.HashMap


class MainActivity : AppCompatActivity() {
    val url = "http://192.168.43.182:8972/"
    var databinding: ActivityMainBindingImpl? = null
    val parameters: HashMap<String, String> = HashMap<String, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val serviceIntent: Intent = Intent(this, CarRemoteService::class.java)
        serviceIntent.putExtra("Car", true)
        startService(serviceIntent)

        val listener = OnClickListener {
            when (it.id) {
                forward.id -> {
                    parameters["direction"] = "Forward"
                }
                backward.id -> {
                    parameters["direction"] = "Backward"
                }
                right.id -> {
                    parameters["direction"] = "Right"
                }
                left.id -> {
                    parameters["direction"] = "Left"

                }
                stop.id -> {
                    parameters["direction"] = "Stop"
                }
            }
            var thread: Thread =
                Thread(Runnable { HttpUtils.instance?.postRequest(url, parameters, "", null) })
            thread.start()

        }

        forward.setOnClickListener(listener)
        backward.setOnClickListener(listener)
        left.setOnClickListener(listener)
        right.setOnClickListener(listener)
        stop.setOnClickListener(listener)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> ""
            R.id.connect -> ""

        }
        return super.onOptionsItemSelected(item)
    }

    private fun getScreenWidth(): Int {
        val windowManager: WindowManager =
            this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }


    override fun onDestroy() {
        stopService(Intent(this, CarRemoteService::class.java))
        super.onDestroy()
    }


}

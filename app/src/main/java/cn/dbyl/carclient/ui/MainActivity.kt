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
import cn.dbyl.carclient.databinding.ActivityMainBinding
import cn.dbyl.carclient.service.CarRemoteService
import cn.dbyl.carclient.utils.HttpUtils
import java.util.HashMap


class MainActivity : AppCompatActivity() {
    val url = "http://192.168.68.88:80/"
    lateinit var databinding: ActivityMainBinding
    private val parameters: HashMap<String, String> = HashMap<String, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val serviceIntent: Intent = Intent(this, CarRemoteService::class.java)
        serviceIntent.putExtra("Car", true)
        startService(serviceIntent)

        val listener = OnClickListener {
            when (it.id) {
                databinding.forward.id -> {
                    parameters["direction"] = "Forward"
                }

                databinding.backward.id -> {
                    parameters["direction"] = "Backward"
                }

                databinding.right.id -> {
                    parameters["direction"] = "Right"
                }

                databinding.left.id -> {
                    parameters["direction"] = "Left"

                }

                databinding.stop.id -> {
                    parameters["direction"] = "Stop"
                }
            }
            var thread =
                Thread { HttpUtils.getInstance()?.getRequest(url, parameters, "") }
            thread.start()
        }

        databinding.forward.setOnClickListener(listener)
        databinding.backward.setOnClickListener(listener)
        databinding.left.setOnClickListener(listener)
        databinding.right.setOnClickListener(listener)
        databinding.stop.setOnClickListener(listener)
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

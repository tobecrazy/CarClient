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
import androidx.activity.result.contract.ActivityResultContract
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.dbyl.carclient.R
import cn.dbyl.carclient.data.Constants
import cn.dbyl.carclient.databinding.ActivityMainBinding
import cn.dbyl.carclient.service.CarRemoteService
import cn.dbyl.carclient.utils.HttpUtils
import cn.dbyl.carclient.viewmodel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.HashMap


class MainActivity : AppCompatActivity() {
    var url = "http://%s/%s"
    lateinit var databinding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel
    private val parameters: HashMap<String, String> = HashMap<String, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        viewModel = MainActivityViewModel(application)
        val serviceIntent: Intent = Intent(this, CarRemoteService::class.java)
        serviceIntent.putExtra("Car", true)
        startService(serviceIntent)
        var targetURL = ""
        val listener = OnClickListener {
            when (it.id) {
                databinding.forward.id -> {
                    targetURL = String.format(url, viewModel.serverIP.value, "Forward")
                }

                databinding.backward.id -> {
                    targetURL = String.format(url, viewModel.serverIP.value, "Backward")
                }

                databinding.right.id -> {
                    targetURL = String.format(url, viewModel.serverIP.value, "Right")
                }

                databinding.left.id -> {
                    targetURL = String.format(url, viewModel.serverIP.value, "Left")

                }

                databinding.stop.id -> {
                    targetURL = String.format(url, viewModel.serverIP.value, "Stop")
                }
            }
            val thread =
                Thread { HttpUtils.getInstance()?.getRequest(targetURL, parameters, "") }
            thread.start()
        }

        viewModel.serverStatus.observe(this, Observer { isEnabled ->
            databinding.forward.isEnabled = isEnabled == true
            databinding.backward.isEnabled = isEnabled == true
            databinding.left.isEnabled = isEnabled == true
            databinding.right.isEnabled = isEnabled == true
            databinding.stop.isEnabled = isEnabled == true
        })

        viewModel.serverIP.observe(this, Observer {
            if (it.isNotBlank()) {
                databinding.serverIp.text = getString(R.string.server_ip, it)
            } else {
                databinding.serverIp.text = String.format(url, viewModel.defaultServerIP)
            }
        })

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
            R.id.setting -> {
                val setting = Intent(this, ChangeServerIP::class.java)
                startActivityForResult(setting, Constants.start_setting_request_code)
            }

            R.id.about -> {
                val about = Intent(this, About::class.java)
                startActivity(about)
            }
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.start_setting_request_code && null != data) {
            val ip = data.getStringExtra(Constants.key_ip)
            Snackbar.make(window.decorView, "Server IP change to $ip", Snackbar.LENGTH_LONG).show()
            viewModel.updateServerStatus(true, ip)
        } else {
            viewModel.updateServerStatus(true, null)
            Snackbar.make(window.decorView, "Not change server IP", Snackbar.LENGTH_LONG).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}

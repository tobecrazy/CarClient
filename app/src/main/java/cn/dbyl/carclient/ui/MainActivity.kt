package cn.dbyl.carclient.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View.OnClickListener
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.dbyl.carclient.R
import cn.dbyl.carclient.data.Constants
import cn.dbyl.carclient.databinding.ActivityMainBinding
import cn.dbyl.carclient.service.CarRemoteService
import cn.dbyl.carclient.utils.HttpUtils
import cn.dbyl.carclient.utils.NetWorkUtils
import cn.dbyl.carclient.viewmodel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private var url = "http://%s/%s"
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private val parameters: HashMap<String, String> = HashMap()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = MainActivityViewModel(application)
        viewModel.initialize(this)
        val localDataIp = viewModel.localDataObjectBox.query().build().findFirst()?.ip
        if (null != localDataIp && NetWorkUtils.isValidIPv4Address(localDataIp)) {
            binding.serverIp.text = getString(R.string.server_ip, localDataIp)
            viewModel.enableControlUI(true)
        }
        val serviceIntent = Intent(this, CarRemoteService::class.java)
        serviceIntent.putExtra("Car", true)
        startService(serviceIntent)
        var targetURL = ""
        val listener = OnClickListener {
            when (it.id) {
                binding.forward.id -> {
                    targetURL = String.format(url, viewModel.serverIP.value, "Forward")
                }

                binding.backward.id -> {
                    targetURL = String.format(url, viewModel.serverIP.value, "Backward")
                }

                binding.right.id -> {
                    targetURL = String.format(url, viewModel.serverIP.value, "Right")
                }

                binding.left.id -> {
                    targetURL = String.format(url, viewModel.serverIP.value, "Left")

                }

                binding.stop.id -> {
                    targetURL = String.format(url, viewModel.serverIP.value, "Stop")
                }
            }
            val thread =
                Thread {
                    val result =
                        HttpUtils.getInstance()?.getRequest(targetURL, parameters, "")
                    viewModel.updateServerResponseData(result)
                }
            thread.start()
        }

        viewModel.serverStatus.observe(this) { isEnabled ->
            binding.forward.isEnabled = isEnabled == true
            binding.backward.isEnabled = isEnabled == true
            binding.left.isEnabled = isEnabled == true
            binding.right.isEnabled = isEnabled == true
            binding.stop.isEnabled = isEnabled == true
        }

        viewModel.serverIP.observe(this) {
            if (it.isNotBlank()) {
                val isEmpty = viewModel.localDataObjectBox.isEmpty
                if (isEmpty) {
                    binding.serverIp.text = getString(R.string.initial_server_ip)
                    return@observe
                }
            } else {
                binding.serverIp.text = String.format(url, viewModel.defaultServerIP)
            }
        }

        viewModel.serverResponseData.observe(this) {
            if (null == it) {
                Snackbar.make(
                    window.decorView,
                    "Failed to connect ${viewModel.serverIP.value}",
                    Snackbar.LENGTH_LONG
                ).show()
                return@observe
            }
            when (it.status) {
                200 -> {}
                else -> {
                    Snackbar.make(
                        window.decorView,
                        "Fail to get Response, get ${it.status}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
        binding.forward.setOnClickListener(listener)
        binding.backward.setOnClickListener(listener)
        binding.left.setOnClickListener(listener)
        binding.right.setOnClickListener(listener)
        binding.stop.setOnClickListener(listener)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                val setting = Intent(this, ChangeServerIP::class.java)
                setting.putExtra(Constants.key_default_ip, viewModel.defaultServerIP)
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

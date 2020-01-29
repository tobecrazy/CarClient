package cn.dbyl.carclient.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.dbyl.carclient.R
import cn.dbyl.carclient.data.Constant
import cn.dbyl.carclient.data.IpMap
import cn.dbyl.carclient.databinding.ActivityMainBinding
import cn.dbyl.carclient.service.CarRemoteService
import cn.dbyl.carclient.utils.SharedPreferencesUtils
import cn.dbyl.carclient.viewmodel.MainActivityViewModel
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainActivityViewModel
    lateinit var databinding: ActivityMainBinding
    val parameters: HashMap<String, String> = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initialViewModel()
        val serviceIntent: Intent = Intent(this, CarRemoteService::class.java)
        serviceIntent.putExtra("Car", true)
        startService(serviceIntent)
        val listener = OnClickListener {
            when (it.id) {
                databinding.forward.id -> {
                    parameters[Constant.DIRECTION] = "Forward"
                }
                databinding.backward.id -> {
                    parameters[Constant.DIRECTION] = "Backward"
                }
                databinding.right.id -> {
                    parameters[Constant.DIRECTION] = "Right"
                }
                databinding.left.id -> {
                    parameters[Constant.DIRECTION] = "Left"

                }
                databinding.stop.id -> {
                    parameters[Constant.DIRECTION] = "Stop"
                }
            }
            viewModel.initialViewModel(parameters)
        }

        viewModel.initialViewModel(parameters)
        databinding.forward.setOnClickListener(listener)
        databinding.backward.setOnClickListener(listener)
        databinding.left.setOnClickListener(listener)
        databinding.right.setOnClickListener(listener)
        databinding.stop.setOnClickListener(listener)
    }

    private fun initialViewModel() {
        Log.d(TAG, "status changed ===>initialViewModel ")
        viewModel = obtainViewModel()
        viewModel.buttonStatusLiveData.observe(this, Observer {
            if (null != it && it.status == 200) {
                updateUI(true)
            } else {
                updateUI(false)
            }
        })

        viewModel.ipMapEvent.observe(this, Observer {
            if (null != it) {
                it.ip
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> setConnectionInfo()
            R.id.connect -> wifiSetting()
            R.id.open_cv -> {
                startOpenCVActivity()
            }

            R.id.blue_tooth_setting -> {
                startBlueToothSettingActivity()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun startBlueToothSettingActivity() {
        val blueToothSettingIntent = Intent(this, BlueToothSettingsActivity::class.java)
        startActivityForResult(blueToothSettingIntent, Constant.BLUETOOTH)
    }

    private fun startOpenCVActivity() {
        val opencv = Intent(this, OpenCVActivity::class.java)
        opencv.putExtra("TAG", "TAG")
        startActivity(opencv)
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


    fun obtainViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this).get(MainActivityViewModel::class.java) as MainActivityViewModel
    }

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    fun updateUI(isEnable: Boolean) {
        databinding.forward?.isEnabled = isEnable
        databinding.backward?.isEnabled = isEnable
        databinding.left?.isEnabled = isEnable
        databinding.right?.isEnabled = isEnable
        databinding.stop?.isEnabled = isEnable
    }

    private fun wifiSetting() {
        val intent = Intent()
        intent.action = "android.net.wifi.PICK_WIFI_NETWORK"
        startActivity(intent)
    }

    @SuppressLint("InflateParams")
    private fun setConnectionInfo() {
        val view: View = layoutInflater.inflate(R.layout.setting_dialog_layout, null)
        val setIpEditText = view.findViewById<EditText>(R.id.set_ip_address)
        setIpEditText.setText(SharedPreferencesUtils(this).getInstance().getIpInfo().ip)
        val setPortEditText = view.findViewById<EditText>(R.id.set_port)
        setPortEditText.setText(SharedPreferencesUtils(this).getInstance().getIpInfo().port.toString())
        val builder = AlertDialog.Builder(this)
            .setView(view)
            .setTitle(R.string.menu_title)
            .setIcon(R.drawable.ic_launcher_background)
            .setPositiveButton(
                R.string.confirm
            ) { _, which ->
                run {
                    Log.d(TAG, "===>which $which  ${setIpEditText.text} + ${setPortEditText.text}")
                    val ipMap = IpMap("", 1)
                    ipMap.ip = setIpEditText.text.toString()
                    ipMap.port = setPortEditText.text.toString().toInt()
                    viewModel.saveIpInfo(ipMap)
                    showMessage("Save successful")
                }
            }
            .setNegativeButton(
                R.string.cancel
            ) { _, which -> Log.d(TAG, "===>which $which  Cancel ") }
        builder.create().show()
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}

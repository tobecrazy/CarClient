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
    val url = "http://192.168.68.88:80/"
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

        viewModel.serverStatus.observe(this, Observer { isEnabled ->
            databinding.forward.isEnabled = isEnabled == true
            databinding.backward.isEnabled = isEnabled == true
            databinding.left.isEnabled = isEnabled == true
            databinding.right.isEnabled = isEnabled == true
            databinding.stop.isEnabled = isEnabled == true
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
            Snackbar.make(window.decorView, "Server IP change to ", Snackbar.LENGTH_LONG).show()
            viewModel.updateServerStatus(true)
        } else {
            viewModel.updateServerStatus(true)
            Snackbar.make(window.decorView, "Not change server IP", Snackbar.LENGTH_LONG).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}

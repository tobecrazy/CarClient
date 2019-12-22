package cn.dbyl.carclient.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.OnClickListener
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.dbyl.carclient.R
import cn.dbyl.carclient.databinding.ActivityMainBinding
import cn.dbyl.carclient.service.CarRemoteService
import cn.dbyl.carclient.viewmodel.MainActivityViewModel
import java.util.HashMap


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
                    parameters["direction"] = "Forward"
                }
                databinding.backward.id -> {
                    parameters["direction"] = "Backward"
                }
                databinding.right?.id -> {
                    parameters["direction"] = "Right"
                }
                databinding.left.id -> {
                    parameters["direction"] = "Left"

                }
                databinding.stop.id -> {
                    parameters["direction"] = "Stop"
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
            if (null != it) {
                Log.d(TAG, "status changed ===>${it.status} ")
                if (it.status == 200) {
                    databinding.forward?.isEnabled = true
                    databinding.backward?.isEnabled = true
                    databinding.left?.isEnabled = true
                    databinding.right?.isEnabled = true
                    databinding.stop?.isEnabled = true
                } else {
                    databinding.forward?.isEnabled = false
                    databinding.backward?.isEnabled = false
                    databinding.left?.isEnabled = false
                    databinding.right?.isEnabled = false
                    databinding.stop?.isEnabled = false
                }
            }else
            {
                Log.d(TAG, "status changed ===>null value ")
            }
        })
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


    fun obtainViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this).get(MainActivityViewModel::class.java) as MainActivityViewModel
    }

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }
}

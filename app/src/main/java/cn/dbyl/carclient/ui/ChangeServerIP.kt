package cn.dbyl.carclient.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import cn.dbyl.carclient.R
import cn.dbyl.carclient.data.Constants
import cn.dbyl.carclient.databinding.ActivityChangeServerIpBinding
import cn.dbyl.carclient.utils.NetWorkUtils
import com.google.android.material.snackbar.Snackbar

class ChangeServerIP : AppCompatActivity() {
    lateinit var binding: ActivityChangeServerIpBinding
    lateinit var ip: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_server_ip)
        binding.input.addTextChangedListener { it ->
            if ((it?.length ?: 0) > 0) {
                ip = binding.input.text.toString()
                binding.confirmButton.isEnabled = true
            } else {
                binding.confirmButton.isEnabled = false
            }
        }
        binding.confirmButton.setOnClickListener {
            if (NetWorkUtils.isValidIPv4Address(ip)) {
                finish()
            } else {
                Snackbar.make(
                    window.decorView,
                    "IP is $ip, Invalid IP Address! ",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }


    override fun finish() {
        val finishIntent = Intent()
        finishIntent.putExtra(Constants.key_ip, ip)
        setResult(Activity.RESULT_OK, finishIntent)
        super.finish()
    }
}
package cn.dbyl.carclient.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import cn.dbyl.carclient.R
import cn.dbyl.carclient.data.Constants
import cn.dbyl.carclient.databinding.ActivityChangeServerIpBinding
import cn.dbyl.carclient.utils.NetWorkUtils
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.Delegates

class ChangeServerIP : AppCompatActivity() {
    lateinit var binding: ActivityChangeServerIpBinding
    var ip: String by Delegates.notNull()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_server_ip)
        //default IP
        ip = intent.getStringExtra(Constants.key_default_ip).toString()
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
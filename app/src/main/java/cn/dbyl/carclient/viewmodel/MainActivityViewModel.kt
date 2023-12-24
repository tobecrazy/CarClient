package cn.dbyl.carclient.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Create by Young on 12/24/2023
 **/
class MainActivityViewModel(val application: Application) : ViewModel() {
    val defaultServerIP = "192.168.68.88"
    val serverStatus = MutableLiveData<Boolean>()
    val serverIP = MutableLiveData<String>()
    fun updateServerStatus(isEnabled: Boolean?, ip: String?) {
        serverStatus.postValue(isEnabled == true)
        if (ip.isNullOrBlank()) {
            serverIP.postValue(defaultServerIP)
        } else {
            serverIP.postValue(ip)
        }
    }
}
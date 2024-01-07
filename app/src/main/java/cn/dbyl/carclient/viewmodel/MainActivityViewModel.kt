package cn.dbyl.carclient.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.dbyl.carclient.ClientApp
import cn.dbyl.carclient.data.model.LocalData
import cn.dbyl.carclient.data.model.MyObjectBox
import cn.dbyl.carclient.utils.DataManager
import cn.dbyl.model.DataModel
import io.objectbox.Box

/**
 * Create by Young on 12/24/2023
 **/
class MainActivityViewModel(val application: Application) : ViewModel() {
    lateinit var localDataObjectBox: Box<LocalData>
    val defaultServerIP: String = "192.168.68.88"
    val defaultServerPort: Int = 80
    val serverStatus = MutableLiveData<Boolean>()
    val serverIP = MutableLiveData<String>()
    val serverResponseData = MutableLiveData<DataModel>()

    fun initialize(context: Context) {
        localDataObjectBox = DataManager.initialize(context).boxFor(LocalData::class.java)
        localDataObjectBox.put(LocalData(0, defaultServerIP, 80))
    }

    fun enableControlUI(isEnabled: Boolean?) {
        isEnabled?.let {
            serverStatus.postValue(isEnabled == true)
        }
    }

    fun updateServerStatus(isEnabled: Boolean?, ip: String?, port: Int = defaultServerPort) {
        enableControlUI(isEnabled)
        if (ip.isNullOrBlank()) {
            serverIP.postValue(defaultServerIP)
            localDataObjectBox.removeAll()
            localDataObjectBox.put(LocalData(0, defaultServerIP, defaultServerPort))
        } else {
            serverIP.postValue(ip)
            localDataObjectBox.removeAll()
            localDataObjectBox.put(LocalData(0, ip, port))
        }
    }

    fun updateServerResponseData(data: MutableLiveData<DataModel>?) {
        if (data != null) {
            serverResponseData.postValue(data.value)
        }
    }
}
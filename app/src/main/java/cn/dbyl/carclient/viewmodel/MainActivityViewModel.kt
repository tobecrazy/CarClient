package cn.dbyl.carclient.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Create by Young on 12/24/2023
 **/
class MainActivityViewModel(val application: Application) : ViewModel() {
    val serverStatus = MutableLiveData<Boolean>()
    fun updateServerStatus(isEnabled: Boolean?) {
        serverStatus.postValue(isEnabled == true)
    }
}
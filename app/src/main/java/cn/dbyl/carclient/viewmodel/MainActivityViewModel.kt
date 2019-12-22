package cn.dbyl.carclient.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cn.dbyl.carclient.data.CarControlRepository

import cn.dbyl.carclient.data.Constant
import cn.dbyl.carclient.network.GetNetworkStatus

import cn.dbyl.carclient.utils.NetWorkUtils
import cn.dbyl.model.DataModel
import java.util.HashMap

/**
 * Create by Young on 12/20/2019
 **/

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    var isNetworkEnable: MutableLiveData<Boolean> = MutableLiveData()
    var buttonStatusLiveData: MutableLiveData<DataModel> = MutableLiveData<DataModel>()
    var repo= CarControlRepository()
    fun initialViewModel(parameters: HashMap<String, String>) {
        val dataModel = GetNetworkStatus().getNetworkStatus(Constant.baidu, parameters).value
        if (null != dataModel) {
            Log.d(TAG, "MainActivityViewModel status changed ===>${dataModel.status} ")
            buttonStatusLiveData.postValue(dataModel)
        } else {
            buttonStatusLiveData.postValue(null)
            Log.d(TAG, "MainActivityViewModel status changed ===>null ")
        }
    }

    companion object {
        val TAG = MainActivityViewModel::class.java.simpleName
    }
}
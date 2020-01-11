package cn.dbyl.carclient.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cn.dbyl.carclient.data.CarControlRepository
import cn.dbyl.carclient.data.Constant
import cn.dbyl.carclient.data.Constant.DIRECTION
import cn.dbyl.carclient.data.SingleLiveEvent
import cn.dbyl.carclient.database.DataManager
import cn.dbyl.carclient.database.NetworkInfo

import cn.dbyl.model.DataModel
import io.objectbox.Box
import java.lang.Exception
import java.util.HashMap
import kotlin.concurrent.thread

/**
 * Create by Young on 12/20/2019
 **/

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    var isNetworkEnable: SingleLiveEvent<Boolean> = SingleLiveEvent<Boolean>()
    var buttonStatusLiveData: MutableLiveData<DataModel> = MutableLiveData<DataModel>()
    var isForwardEnable: SingleLiveEvent<Boolean> = SingleLiveEvent<Boolean>()
    var isBackwardEnable: SingleLiveEvent<Boolean> = SingleLiveEvent<Boolean>()
    var isLeftEnable: SingleLiveEvent<Boolean> = SingleLiveEvent<Boolean>()
    var isRightEnable: SingleLiveEvent<Boolean> = SingleLiveEvent<Boolean>()
    var isStopEnable: SingleLiveEvent<Boolean> = SingleLiveEvent<Boolean>()
    var isTakePictureEnable: SingleLiveEvent<Boolean> = SingleLiveEvent<Boolean>()

    var networkInfoEntityBox: Box<NetworkInfo>? = null
    var repo = CarControlRepository()

    fun initialViewModel(parameters: HashMap<String, String>) {
        if (parameters.isEmpty() && null == parameters[DIRECTION]) {
            return
        }
        val call = repo.getAPIService().getServerStatus(parameters[DIRECTION]!!)
        if (!call.isExecuted) {
            buttonStatusLiveData.postValue(null)
            var dataModel: DataModel = DataModel()
            thread {
                Log.d(TAG, "Get data request!")
                try {
                    var res = call.execute()
                    dataModel.status = res.code()
                    dataModel.result = res.message()
                    dataModel.message = res.body()
                    buttonStatusLiveData.postValue(dataModel)
                    Log.d(TAG, "Request success ${res.message()}")
                } catch (e: Exception) {
                    Log.d(TAG, "Failed to get data ${e.message}")
                    buttonStatusLiveData.postValue(dataModel)
                }
            }
        }

    }

    fun getNetworkStatus(): Boolean {
        return isNetworkEnable.value ?: false
    }

    companion object {
        val TAG = MainActivityViewModel::class.java.simpleName
    }

    init {
        networkInfoEntityBox = DataManager().getInstance()?.networkInfoEntityBox

    }
}
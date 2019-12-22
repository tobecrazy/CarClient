package cn.dbyl.carclient.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.dbyl.model.DataModel
import java.lang.Exception
import java.util.HashMap

/**
 * Create by i321533 (young.liu@sap.com) on 12/20/2019
 **/
class GetNetworkStatus {
    var liveData: MutableLiveData<DataModel>? = MutableLiveData<DataModel>()
    companion object{
        val TAG=GetNetworkStatus::class.java.simpleName
    }
    fun getNetworkStatus(
        url: String,
        parameters: HashMap<String, String>
    ): MutableLiveData<DataModel> {
        var dataModel: DataModel? = DataModel()
//        var thread: Thread =
//            Thread(Runnable {
//                data = HttpUtils.instance?.postRequest(url, parameters, "", null)
//                dataModel = HttpUtils.instance?.getRequest(url, parameters, null)
//                Log.d(TAG," dataModel is ==>${dataModel.toString()} ")
//                liveData?.postValue(dataModel)
//            })
        try {
//            thread.start()
//            dataModel = HttpUtils.instance?.getRequest(url, parameters, null)
            Log.d(TAG," dataModel is ==>${dataModel.toString()} ")
            liveData?.postValue(dataModel)
        } catch (e: Exception) {
            dataModel?.status = 500
            dataModel?.message = "Server internal error"
            dataModel?.result = "Server internal error"
            Log.e(TAG,"error ==  ")
            liveData?.postValue(dataModel)
        }
        Log.d(TAG," dataModel is ==>${ dataModel.toString()} ")
        Log.d(TAG,"final data is ==>${liveData?.value.toString()} ")
        return liveData!!
    }
}
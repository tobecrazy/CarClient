package cn.dbyl.carclient.network

import android.util.Log
import cn.dbyl.carclient.utils.HttpUtils
import cn.dbyl.model.DataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.CountDownLatch

/**
 * Create by i321533 (young.liu@sap.com) on 12/20/2019
 **/
class GetNetworkStatus {
    lateinit var countDownLatch: CountDownLatch

    companion object {
        val TAG = GetNetworkStatus::class.java.simpleName
    }

    fun getNetworkStatus(
        url: String,
        parameters: HashMap<String, String>
    ): DataModel?{
        var dataModel: DataModel? = DataModel()
        countDownLatch = CountDownLatch(1)
//        var thread: Thread =
//            Thread(Runnable {
//                var data = HttpUtils.instance?.postRequest(url, parameters, "", null)
//                liveData?.postValue(data?.value)
//            })
        try {
//            thread.start()
//            countDownLatch.countDown()
            GlobalScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.IO) {
                    dataModel = HttpUtils.instance?.postRequest(url, parameters, "", null)
                }
            }

        } catch (e: Exception) {
            dataModel?.status = 500
            dataModel?.message = "Server internal error"
            dataModel?.result = "Server internal error"
            Log.e(TAG, "error ==  ")
        }
        Log.d(TAG, "final data is ==>${dataModel.toString()} ")
        return dataModel
    }


}
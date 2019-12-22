package cn.dbyl.carclient.network

import cn.dbyl.model.DataModel
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Create by i321533 (young.liu@sap.com) on 12/21/2019
 **/
class RemoteControlServiceImpl:RemoteControlService {
    override fun getCall(id: Int): Call<ResponseBody?>? {
       return null
    }

    override fun getUser(): Call<DataModel>? {
       return null
    }
}
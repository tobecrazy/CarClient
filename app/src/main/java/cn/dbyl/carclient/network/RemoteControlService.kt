package cn.dbyl.carclient.network

import cn.dbyl.model.DataModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path


/**
 * Create by Young on 12/21/2019
 **/
interface RemoteControlService {
    @Headers("Authorization: authorization")
    @GET("/a")
    open fun getCall(@Path("id") id: Int): Call<ResponseBody?>?

    @Headers("Authorization: authorization")
    @GET("user")
    fun getUser():Call<DataModel>?


}
package cn.dbyl.carclient.network
import retrofit2.Call
import retrofit2.http.*


/**
 * Create by Young on 12/21/2019
 **/
interface RemoteControlService {
    @POST("/")
    fun getServerStatus(@Query("direction") direction:String): Call<String>
    
}
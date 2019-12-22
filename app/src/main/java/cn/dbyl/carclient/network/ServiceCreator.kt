package cn.dbyl.carclient.network

import cn.dbyl.carclient.data.Constant.baseURL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Create by young on 11/03/2019
 **/
object ServiceCreator {
    private val httpClientBuilder = OkHttpClient.Builder()
        .connectTimeout(30000, TimeUnit.MILLISECONDS)
        .readTimeout(30000, TimeUnit.MILLISECONDS)
        .writeTimeout(30000, TimeUnit.MILLISECONDS)

    var retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClientBuilder.build())
        .baseUrl(baseURL)
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}
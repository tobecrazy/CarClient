package cn.dbyl.carclient.network

import cn.dbyl.carclient.data.Constant.PORT
import cn.dbyl.carclient.data.Constant.URL
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Create by young on 11/03/2019
 **/
object ServiceCreator {
    private val httpClientBuilder = OkHttpClient.Builder()
        .connectTimeout(30000, TimeUnit.MILLISECONDS)
        .readTimeout(30000, TimeUnit.MILLISECONDS)
        .writeTimeout(30000, TimeUnit.MILLISECONDS)


    private var retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClientBuilder.build())
        .baseUrl(URL)
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}
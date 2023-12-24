package cn.dbyl.carclient.utils

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import cn.dbyl.model.DataModel

import java.io.IOException
import java.util.HashMap
import java.util.concurrent.TimeUnit

import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

/**
 * @author Young
 */
class HttpUtils {
    private val TAG = HttpUtils::class.java.simpleName
    private val resource = MutableLiveData<DataModel>()
    private var client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val contentType = "Content-Type"

    /**
     * @param url
     * @param parameters
     * @return
     * @author Young
     */
    @Throws(IOException::class)
    fun getRequest(
        url: String,
        parameters: HashMap<String, String>,
        credential: String?
    ): MutableLiveData<DataModel> {
        val builder = Headers.Builder()
        builder.add(contentType, "application/json;charset=gb2312")
        if (null != credential) {
            builder.add("Authorization", credential)
        }
        val headers = builder.build()
        return getResponse(url, parameters, headers)
    }

    /**
     * @param url
     * @param parameters
     * @param credential
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun deleteRequest(
        url: String,
        parameters: HashMap<String, String>,
        credential: String?
    ): MutableLiveData<DataModel> {
        val builder = Headers.Builder()
        builder.add(contentType, "application/json;charset=gb2312")
        if (null != credential) {
            builder.add("Authorization", credential)
        }
        val headers = builder.build()
        return deleteResponse(url, parameters, headers)
    }

    /**
     * @param url
     * @param parameters
     * @param body
     * @return
     * @author Young
     */
    fun postRequest(
        url: String,
        parameters: HashMap<String, String>,
        body: String,
        credential: String?
    ): MutableLiveData<DataModel> {
        val builder = Headers.Builder()
        builder.add(contentType, "application/json;charset=gb2312")
        if (null != credential) {
            builder.add("Authorization", credential)
        }
        val headers = builder.build()
        return postResponse(url, parameters, body, headers)
    }

    /**
     * @param url
     * @param parameters
     * @param body
     * @param credential
     * @return
     */
    fun putRequest(
        url: String,
        parameters: HashMap<String, String>,
        body: String,
        credential: String?
    ): MutableLiveData<DataModel> {
        val builder = Headers.Builder()
        builder.add(contentType, "application/json;charset=gb2312")
        if (null != credential) {
            builder.add("Authorization", credential)
        }
        val headers = builder.build()
        return putResponse(url, parameters, body, headers)
    }

    /**
     * @param url
     * @param parameters
     * @param headers
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun getResponse(
        url: String,
        parameters: HashMap<String, String>,
        headers: Headers?
    ): MutableLiveData<DataModel> {
        val builder = Request.Builder()

        val dataModel = DataModel()
        val requestUrl = getRequestUrl(url, parameters)
        Log.v(TAG, "get request url ---> $requestUrl")
        if (headers != null) {
            builder.headers(headers)
        }
        builder.get().url(requestUrl)
        val request = builder.build()
        var response: Response? = null
        try {
            response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body
                dataModel.result = responseBody!!.string()
            } else {
                Log.e(TAG, "return null$url")
            }
            dataModel.status = response.code
            dataModel.message = response.message
        } catch (e: Exception) {
            Log.e(TAG, "Http request error " + e.message)
            dataModel.status = 400
            dataModel.message = null
            dataModel.result = null
        }
        resource.postValue(dataModel)
        return resource
    }

    /***
     *
     * @param url
     * @param parameters
     * @param body
     * @param headers
     * @return
     */
    fun putResponse(
        url: String,
        parameters: HashMap<String, String>,
        body: String,
        headers: Headers?
    ): MutableLiveData<DataModel> {
        val builder = Request.Builder()
        val dataModel = DataModel()
        val requestUrl = getRequestUrl(url, parameters)
        Log.v(TAG, "get request url $requestUrl")
        Log.v(TAG, "get request url $body")
        //transfer to json
        if (headers != null) {
            builder.headers(headers)
        }
        val requestBody = RequestBody.create(JSON, body)
        builder.put(requestBody).url(requestUrl)
        val request = builder.build()
        var response: Response? = null
        try {
            response = client.newCall(request).execute()
            if (response.isSuccessful) {
                dataModel.result = response.body!!.string()
            } else {
                Log.e(TAG, "return null")
            }
            dataModel.status = response.code
            dataModel.message = response.message
        } catch (e: Exception) {
            Log.e(TAG, "Post error ${e.message}")
            dataModel.result = null
            dataModel.status = 400
            dataModel.message = e.message
        }
        resource.value = dataModel
        return resource
    }

    /**
     * @param url
     * @param parameters
     * @param headers
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun deleteResponse(
        url: String,
        parameters: HashMap<String, String>,
        headers: Headers?
    ): MutableLiveData<DataModel> {
        val builder = Request.Builder()
        val dataModel = DataModel()
        val requestUrl = getRequestUrl(url, parameters)
        Log.v(TAG, "get request url ---> $requestUrl")
        if (headers != null) {
            builder.headers(headers)
        }
        builder.get().url(requestUrl)
        val request = builder.delete().build()
        var response: Response? = null
        try {
            response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body
                dataModel.result = responseBody!!.string()
            } else {
                Log.e(TAG, "return null$url")
            }
            dataModel.status = response.code
            dataModel.message = response.message
        } catch (e: Exception) {
            Log.e(TAG, "Http request error " + e.message)
            Log.e(TAG, "Post error ${e.message}")
            dataModel.result = null
            dataModel.status = 400
            dataModel.message = e.message

        }
        resource.value = dataModel
        return resource
    }

    /**
     * @param url
     * @param parameters
     * @param body
     * @param headers
     * @return
     */
    fun postResponse(
        url: String,
        parameters: HashMap<String, String>,
        body: String,
        headers: Headers?
    ): MutableLiveData<DataModel> {
        val builder = Request.Builder()
        val dataModel = DataModel()
        val requestUrl = getRequestUrl(url, parameters)
        Log.v(TAG, "get request url $requestUrl")
        Log.v(TAG, "get request url $body")
        //transfer to json
        if (headers != null) {
            builder.headers(headers)
        }
        val requestBody = RequestBody.create(JSON, body)
        builder.post(requestBody).url(requestUrl)
        val request = builder.build()
        var response: Response? = null
        try {
            response = client.newCall(request).execute()
            if (response.isSuccessful) {
                dataModel.result = response.body!!.string()
            } else {
                Log.e(TAG, "return null")
            }
            dataModel.status = response.code
            dataModel.message = response.message
        } catch (e: Exception) {
            Log.e(TAG, "Post error ${e.message}")
            dataModel.result = null
            dataModel.status = 400
            dataModel.message = e.message
        }
        resource.value = dataModel
        return resource
    }

    /**
     * @param uri
     * @param parameters
     * @return
     * @author Young
     */
    fun getRequestUrl(uri: String, parameters: HashMap<String, String>): String {
        val builder = StringBuilder(uri)
        if (!uri.contains("?")) {
            builder.append("?")
        } else if (!uri.endsWith("?")) {
            builder.append("&")
        }
        for (key in parameters.keys) {
            builder.append(Uri.encode(key, "utf-8")).append("=")
                .append(Uri.encode(parameters[key], "utf-8")).append("&")
        }
        if (builder[builder.length - 1] == '&') {
            builder.deleteCharAt(builder.length - 1)
        }
        if (builder[builder.length - 1] == '?') {
            builder.deleteCharAt(builder.length - 1)
        }
        return builder.toString()
    }

    companion object {
        var httpUtils: HttpUtils? = null
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()

        /**
         * only one instance
         *
         * @return
         */
        fun getInstance(): HttpUtils? {
            if (httpUtils == null) {
                synchronized(HttpUtils::class.java) {
                    if (httpUtils == null) {
                        httpUtils = HttpUtils()
                    }
                }
            }
            return httpUtils
        }
    }
}

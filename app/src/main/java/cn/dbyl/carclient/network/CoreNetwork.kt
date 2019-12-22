package cn.dbyl.carclient.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CoreNetwork {

    private val remoteControlService = ServiceCreator.create(RemoteControlService::class.java)
    suspend fun fetchProvinceList() = remoteControlService.getUser()?.await()


    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
            })
        }
    }

    companion object {

        private var network: CoreNetwork? = null

        fun getInstance(): CoreNetwork {
            if (network == null) {
                synchronized(CoreNetwork::class.java) {
                    if (network == null) {
                        network = CoreNetwork()
                    }
                }
            }
            return network!!
        }

    }

}
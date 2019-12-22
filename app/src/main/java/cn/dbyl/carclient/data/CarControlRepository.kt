package cn.dbyl.carclient.data

import cn.dbyl.carclient.network.CoreNetwork
import cn.dbyl.model.DataModel
import retrofit2.Call

/**
 * Create by Young on 12/21/2019
 **/
class CarControlRepository {
    suspend fun getNetworkStatus(): Boolean {
        var service=CoreNetwork.getInstance().fetchProvinceList()
        return false
    }
}
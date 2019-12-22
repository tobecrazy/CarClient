package cn.dbyl.carclient.data

import cn.dbyl.carclient.network.RemoteControlService
import cn.dbyl.carclient.network.ServiceCreator

/**
 * Create by Young on 12/21/2019
 **/
class CarControlRepository {
    fun getAPIService(): RemoteControlService {
        return ServiceCreator.create(RemoteControlService::class.java)
    }
}
package cn.dbyl.carclient.database

import cn.dbyl.carclient.ClientApp
import io.objectbox.Box
import io.objectbox.BoxStore


/**
 * Create by Young
 **/
class DataManager {
    private var dataManager: DataManager? = null

    @Synchronized
    fun getInstance(): DataManager? {
        if (dataManager == null) {
            dataManager = DataManager()
        }
        return dataManager
    }

    var boxStore: BoxStore? = null
    var networkInfoEntityBox: Box<NetworkInfo>? = null

    fun init(clientApp: ClientApp) {
        boxStore = clientApp.boxStore
        networkInfoEntityBox = initUserEntityBox(NetworkInfo::class.java)
    }

    private fun initUserEntityBox(clazz: Class<NetworkInfo>): Box<NetworkInfo>? {
        return boxStore!!.boxFor(clazz)
    }
}
package cn.dbyl.carclient.utils

import android.content.Context
import android.util.Log
import cn.dbyl.carclient.data.model.MyObjectBox
import com.clj.fastble.BuildConfig
import io.objectbox.BoxStore
import io.objectbox.android.Admin

/**
 * Create by Young on 01/06/2024
 **/
object DataManager {
    lateinit var objectStore: BoxStore
        private set

    fun initialize(content: Context): BoxStore {
        objectStore = MyObjectBox.builder().androidContext(content).build()
        if (BuildConfig.DEBUG) {
            val started = Admin(objectStore).start(content)
            Log.i("ObjectBrowser", "Started: $started")
        }
        return objectStore
    }


}
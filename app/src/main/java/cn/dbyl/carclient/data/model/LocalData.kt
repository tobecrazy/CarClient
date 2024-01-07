package cn.dbyl.carclient.data.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Create by Young on 01/06/2024
 **/
@Entity
data class LocalData(@Id var id: Long = 0, val ip: String, val port: Int)

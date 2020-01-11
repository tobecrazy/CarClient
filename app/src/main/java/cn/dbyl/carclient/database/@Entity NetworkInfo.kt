package cn.dbyl.carclient.database

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.annotation.Unique

/**
 * Create by young
 **/
@Entity
data class NetworkInfo(
    @Id var id: Long, @Unique
    @Index var ip: String, var port: Int
)
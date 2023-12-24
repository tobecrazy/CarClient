package cn.dbyl.model

/**
 * This class is for OKHTTP Response , if request succeeded,
 * status is 200, message is OK , result is the response body
 * @author Young
 */
data class DataModel(var status: Int = 0, var message: String? = null, var result: String? = null) {}

package com.lf.esharing.database.user

import com.lf.esharing.database.purchase.PurchaseEntity
import okhttp3.RequestBody
import org.json.JSONObject

class UserRepository(val userDao: UserDao, val userApi: UserApi = UserClient.getInstance()) {

    suspend fun login(username: String, password: String): Boolean {
        // only use rest api to login
        return userApi.login(mapOf("username" to username, "password" to password)).isSuccessful
    }

    suspend fun getUsers(): List<UserEntity>? {
        return userApi.allUsers().body()
    }

    suspend fun getMembers(username: String): List<String>? {
        return userApi.getMembers(username).body()
    }

    suspend fun getPurchases(username: String): List<PurchaseEntity>? {
        return userApi.getPurchases(username).body()
    }

    suspend fun signup(user: RequestBody): Boolean {
        return userApi.signup(user).isSuccessful
    }

    suspend fun addMember(data: RequestBody): JSONObject? {
        val res = userApi.addMember(data)
        val rsMap:MutableMap<String, String>? = res.body()?.toMutableMap()
        rsMap?.put("code", res.code().toString())
        return JSONObject(rsMap?.toMap())
    }
}
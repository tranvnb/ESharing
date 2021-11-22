package com.lf.esharing.database.user

import com.lf.esharing.network.ClientBuilder

object UserClient {
    private const val url:String = "user/"

    fun getInstance(): UserApi {
        return ClientBuilder.createClient(UserApi::class.java, url)
    }
}
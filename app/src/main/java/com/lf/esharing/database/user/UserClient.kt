package com.lf.esharing.database.user

import com.lf.esharing.network.ClientBuilder

object UserClient {
    private const val url:String = "user/"

    private var INSTANCE: UserApi? = null

    fun getInstance(): UserApi {
        if (INSTANCE == null) {
            synchronized(this){
                INSTANCE = ClientBuilder.createClient(UserApi::class.java, url)
            }
        }
        return INSTANCE!!
    }
}
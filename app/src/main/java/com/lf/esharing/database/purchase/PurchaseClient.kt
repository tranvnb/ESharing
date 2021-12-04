package com.lf.esharing.database.purchase

import com.lf.esharing.database.user.UserApi
import com.lf.esharing.database.user.UserClient
import com.lf.esharing.network.ClientBuilder

object PurchaseClient {
    private const val url:String = "purchase/"

    private var INSTANCE: PurchaseApi? = null

    fun getInstance(): PurchaseApi {
        if (INSTANCE == null) {
            synchronized(this){
                INSTANCE = ClientBuilder.createClient(PurchaseApi::class.java, url)
            }
        }
        return INSTANCE!!
    }
}
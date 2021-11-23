package com.lf.esharing.database.purchase

import com.lf.esharing.database.user.UserApi
import com.lf.esharing.network.ClientBuilder

object PurchaseClient {
    private const val url:String = "purchase/"

    fun getInstance(): PurchaseApi {
        return ClientBuilder.createClient(PurchaseApi::class.java, url)
    }
}
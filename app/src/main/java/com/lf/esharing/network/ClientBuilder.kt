package com.lf.esharing.network


import com.lf.esharing.utils.MoshiHelper.moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ClientBuilder {
    val baseUrl = "http://172.30.75.229:5000/"



    fun <T> createClient(clientClass: Class<T>, url: String) : T {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("$baseUrl/$url")
            .build()
            .create(clientClass)
    }

}
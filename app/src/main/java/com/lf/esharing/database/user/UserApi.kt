package com.lf.esharing.database.user

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserApi {
    @GET("all")
    suspend fun allUsers() : Response<List<UserEntity>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("login")
    suspend fun login(@Body body: Map<String, String>): Response<Map<String, String>>
}
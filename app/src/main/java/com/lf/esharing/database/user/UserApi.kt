package com.lf.esharing.database.user

import com.lf.esharing.database.purchase.PurchaseEntity
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("all")
    suspend fun allUsers() : Response<List<UserEntity>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("login")
    suspend fun login(@Body body: Map<String, String>): Response<Map<String, String>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("{username}/members")
    suspend fun getMembers(@Path("username") username: String): Response<List<String>> // get list member of this user

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("{username}/purchases")
    suspend fun getPurchases(@Path("username") username: String): Response<List<PurchaseEntity>> // get list purchase of this user

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("signup")
    suspend fun signup(@Body user: RequestBody): Response<Map<String, String>>

}
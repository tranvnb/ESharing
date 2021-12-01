package com.lf.esharing.database.purchase

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.util.*


interface PurchaseApi {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("{member}/{id}")
    suspend fun getPurchaseInfoOfMember(@Path("id") id: UUID, @Path("member") member: String, @Body data: Map<String, String>): Response<PurchaseEntity>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @DELETE("{id}")
    suspend fun deletePurchase(@Path("id") id: UUID, @Body request: PurchasesRequest): Response<Map<String, String>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @PUT("{id}")
    suspend fun updatePurchase(@Path("id") id: UUID, @Body request: PurchasesRequest): Response<Map<String, String>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("add")
    suspend fun insertPurchase(@Body request: RequestBody): Response<Map<String, String>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @DELETE("all")
    suspend fun deleteAll(@Body request: Map<String, String>): Response<Map<String, String>>

}

package com.lf.esharing.database.purchase

class PurchasesRequest(
    val username: String,
    val password: String,
    val purchase: PurchaseEntity?
){
}
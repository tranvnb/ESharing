package com.lf.esharing.database.purchase

class PurchasesRequest(
    val username: String,
    val password: String,
    val purchase: PurchaseEntity?
){
    var purchases: List<PurchaseEntity>? = null

    constructor(username: String, password: String, purchase: PurchaseEntity?, purchases: List<PurchaseEntity>?): this(username, password, purchase) {
        this.purchases = purchases
    }
}
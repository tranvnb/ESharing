package com.lf.esharing.utils

import android.text.TextUtils

object Validator {
    fun inputCheck(
        purchaseType: String,
        purchaseDate: String,
        storeName: String,
        storeLoc: String,
        purTotal: Double?
    ): Boolean{
        return !(TextUtils.isEmpty(purchaseType) || TextUtils.isEmpty(purchaseDate) || TextUtils.isEmpty(storeName) || TextUtils.isEmpty(storeLoc) || purTotal == null)
    }
}
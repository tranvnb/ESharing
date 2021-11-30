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

    fun registrationInputCheck(username: String, password: String, confirm: String, firstname: String, lastname: String): Boolean {
        return !(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(confirm) || TextUtils.isEmpty(firstname)
                || TextUtils.isEmpty(lastname) || confirm != password)
    }
}
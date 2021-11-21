package com.example.expensemanager.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "purchase_table")
data class PurchaseEntity (
    @PrimaryKey(autoGenerate = true)
    val purchaseId: Int,

    val purchaseType: String,
    val purcdate: String,
    val storename: String,
    val storelocation: String,
    val itemspurchased: String,
    val totalcost: String
        ) : Parcelable
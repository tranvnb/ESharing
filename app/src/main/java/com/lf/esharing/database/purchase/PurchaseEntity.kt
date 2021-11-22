package com.lf.esharing.database.purchase

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "Purchases")
@Parcelize
class PurchaseEntity (

    @Json(name = "type")
    @ColumnInfo(name = "type")
    var purchaseType: String = "",

    @ColumnInfo(name = "store")
    @Json(name = "store")
    var storename: String = "",

    @Json(name = "location")
    @ColumnInfo(name = "location")
    var storelocation: String = "",

    @Json(name = "item")
    @ColumnInfo(name = "item")
    var itemspurchased: String = "",

    @Json(name = "total")
    @ColumnInfo(name = "total")
    var totalcost: Double = 0.0,

    @Json(name = "date")
    @ColumnInfo(name = "date")
    var purcdate: LocalDateTime? = null

) : Parcelable {
    @PrimaryKey()
    var id: UUID = UUID.randomUUID()

}
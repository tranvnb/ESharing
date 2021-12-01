package com.lf.esharing.database.purchase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "Purchases")
data class PurchaseEntity(
    @PrimaryKey()
    @Json(name = "id")
    @ColumnInfo(name = "id")
    var id: UUID = UUID.randomUUID()
) {

    @ColumnInfo(name = "store")
    @Json(name = "store")
    var storename: String = ""

    @Json(name = "location")
    @ColumnInfo(name = "location")
    var storelocation: String = ""

    @Json(name = "item")
    @ColumnInfo(name = "item")
    var itemspurchased: String = ""

    @Json(name = "total")
    @ColumnInfo(name = "total")
    var totalcost: Double = 0.0

    @Json(name = "date")
    @ColumnInfo(name = "date")
    var purcdate: LocalDateTime? = null

    constructor(
        storename: String,
        storelocation: String,
        itemspurchased: String,
        totalcost: Double,
        purdate: LocalDateTime
    ) : this() {
        this.storename = storename
        this.storelocation = storelocation
        this.itemspurchased = itemspurchased
        this.totalcost = totalcost
        this.purcdate = purdate
    }

}
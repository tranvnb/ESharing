package com.lf.esharing.database.user

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lf.esharing.database.purchase.PurchaseEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "Users")
@Parcelize
@JsonClass(generateAdapter = true)
class UserEntity (
    @PrimaryKey()
    @Json(name = "id")
    var id: String, // java.util.UUID = UUID.randomUUID()

    @Json(name = "username")
    @ColumnInfo(name = "username")
    var username: String = "",

    @ColumnInfo(name = "password")
    @Json(name = "password")
    var password: String = "",

    @Json(name = "first_name")
    @ColumnInfo(name = "first_name")
    var firstName: String = "",

    @Json(name = "last_name")
    @ColumnInfo(name = "last_name")
    var lastName: String = "",

    @Json(name = "is_owner")
    @ColumnInfo(name = "is_owner")
    var isOwner: Boolean = true, //by default, each one is the owner of their own. Only change when they want to be add to other owner's member list

//    @Json(name = "members")
//    @ColumnInfo(name = "members")
//    var members: MutableList<String>, // = mutableListOf<String>(),
//
//    @Json(name = "purchases")
//    @ColumnInfo(name = "purchases")
//    var purchases: MutableList<PurchaseEntity> // = mutableListOf<PurchaseEntity>()

) : Parcelable {
}
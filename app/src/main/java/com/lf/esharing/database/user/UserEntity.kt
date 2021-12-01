package com.lf.esharing.database.user

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@Entity(tableName = "Users")
data class UserEntity(
    @PrimaryKey()
    @Json(name = "id")
    @ColumnInfo(name = "id")
    var id: UUID = UUID.randomUUID()
) {

    @Json(name = "username")
    @ColumnInfo(name = "username")
    var username: String = ""

    @ColumnInfo(name = "password")
    @Json(name = "password")
    var password: String = ""

    @Json(name = "first_name")
    @ColumnInfo(name = "first_name")
    var firstName: String = ""

    @Json(name = "last_name")
    @ColumnInfo(name = "last_name")
    var lastName: String = ""

    @Json(name = "is_owner")
    @ColumnInfo(name = "is_owner")
    var isOwner: Boolean = true

    constructor(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        isOwner: Boolean
    ) : this() {
        this.username = username
        this.password = password
        this.firstName = firstName
        this.lastName = lastName
        this.isOwner = isOwner
    }

}
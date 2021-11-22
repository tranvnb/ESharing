package com.lf.esharing.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class UUIDAdapter {
    @ToJson
    fun toJson(value: UUID): String {
        return value.toString()
    }

    @FromJson
    fun fromJson(value: String): UUID {
        return UUID.fromString(value)
    }

}
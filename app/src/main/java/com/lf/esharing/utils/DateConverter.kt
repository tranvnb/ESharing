package com.lf.esharing.utils

import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateConverter {
    @TypeConverter
    fun fromLocalDateTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value, FORMATTER)
    }

    @TypeConverter
    fun localDateTimeToString(date: LocalDateTime): String {
        return FORMATTER.format(date)
    }

    companion object {
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
    }
}
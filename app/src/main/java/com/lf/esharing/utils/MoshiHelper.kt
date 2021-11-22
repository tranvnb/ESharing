package com.lf.esharing.utils

import com.lf.esharing.database.user.UserEntity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MoshiHelper {

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(DateAdapter())
        .add(UUIDAdapter())
        .build()

    fun <T> fromJson(classEntity: Class<T>, str: String): List<T>? {
        val type = Types.newParameterizedType(List::class.java, classEntity)
        val adapter: JsonAdapter<List<T>> = moshi.adapter(type)
        return adapter.fromJson(str)?.toList()
    }

    fun <T> toJson(classEntity: Class<T>, list: List<T>?): String {
        val type = Types.newParameterizedType(List::class.java, classEntity)
        val adapter: JsonAdapter<List<T>> = moshi.adapter(type)
        return adapter.toJson(list)
    }
}
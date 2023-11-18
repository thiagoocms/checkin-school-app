package com.nassau.checkinschool.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object JsonParse {
    private val g: Gson? = null
    private val gson: Gson = (GsonBuilder()).create()

    @Throws(IllegalAccessException::class)
    fun fromJson(json: String?, clazz: Class<*>?): Any {
        val result: Any = gson.fromJson(json, clazz)
        return result
    }

    @Throws(IllegalAccessException::class)
    fun toJson(data: Any?): String {
        val jsonValue: String = gson.toJson(data)
        return jsonValue
    }
}
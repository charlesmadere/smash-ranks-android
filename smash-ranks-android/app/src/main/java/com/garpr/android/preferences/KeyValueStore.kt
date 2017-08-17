package com.garpr.android.preferences

interface KeyValueStore {

    val all: Map<String, *>?

    fun clear()

    operator fun contains(key: String): Boolean

    fun getBoolean(key: String, fallbackValue: Boolean): Boolean

    fun getFloat(key: String, fallbackValue: Float): Float

    fun getInteger(key: String, fallbackValue: Int): Int

    fun getLong(key: String, fallbackValue: Long): Long

    fun getString(key: String, fallbackValue: String?): String

    fun remove(key: String)

    fun setBoolean(key: String, value: Boolean)

    fun setFloat(key: String, value: Float)

    fun setInteger(key: String, value: Int)

    fun setLong(key: String, value: Long)

    fun setString(key: String, value: String)

}

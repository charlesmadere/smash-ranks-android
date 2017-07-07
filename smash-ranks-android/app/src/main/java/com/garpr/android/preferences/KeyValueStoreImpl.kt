package com.garpr.android.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import java.util.Collections

class KeyValueStoreImpl(
        private val mApplication: Application,
        private val mName: String
) : KeyValueStore {

    override fun clear() {
        sharedPreferences.edit()
                .clear()
                .apply()
    }

    override fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    override val all: Map<String, *>?
        get() {
            val map = sharedPreferences.all

            if (map == null || map.isEmpty()) {
                return null
            } else {
                return Collections.unmodifiableMap(map)
            }
        }

    override fun getBoolean(key: String, fallbackValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, fallbackValue)
    }

    override fun getFloat(key: String, fallbackValue: Float): Float {
        return sharedPreferences.getFloat(key, fallbackValue)
    }

    override fun getInteger(key: String, fallbackValue: Int): Int {
        return sharedPreferences.getInt(key, fallbackValue)
    }

    override fun getLong(key: String, fallbackValue: Long): Long {
        return sharedPreferences.getLong(key, fallbackValue)
    }

    override fun getString(key: String, fallbackValue: String?): String {
        return sharedPreferences.getString(key, fallbackValue)
    }

    private val sharedPreferences: SharedPreferences
        get() = mApplication.getSharedPreferences(mName, Context.MODE_PRIVATE)

    override fun remove(key: String) {
        sharedPreferences.edit()
                .remove(key)
                .apply()
    }

    override fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit()
                .putBoolean(key, value)
                .apply()
    }

    override fun setFloat(key: String, value: Float) {
        sharedPreferences.edit()
                .putFloat(key, value)
                .apply()
    }

    override fun setInteger(key: String, value: Int) {
        sharedPreferences.edit()
                .putInt(key, value)
                .apply()
    }

    override fun setLong(key: String, value: Long) {
        sharedPreferences.edit()
                .putLong(key, value)
                .apply()
    }

    override fun setString(key: String, value: String) {
        sharedPreferences.edit()
                .putString(key, value)
                .apply()
    }

}

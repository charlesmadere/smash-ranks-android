package com.garpr.android.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.garpr.android.preferences.KeyValueStore.BatchEditor
import java.util.*

class KeyValueStoreImpl(
        private val application: Application,
        private val name: String
) : KeyValueStore {

    override val all: Map<String, *>?
        get() {
            val map = sharedPreferences.all

            return if (map.isNullOrEmpty()) {
                null
            } else {
                Collections.unmodifiableMap(map)
            }
        }

    override fun batchEdit(): BatchEditor {
        return BatchEditorImpl(sharedPreferences.edit())
    }

    override fun clear() {
        sharedPreferences.edit()
                .clear()
                .apply()
    }

    override fun contains(key: String) = key in sharedPreferences

    override fun getBoolean(key: String, fallbackValue: Boolean) =
        sharedPreferences.getBoolean(key, fallbackValue)

    override fun getFloat(key: String, fallbackValue: Float) =
        sharedPreferences.getFloat(key, fallbackValue)

    override fun getInteger(key: String, fallbackValue: Int) =
        sharedPreferences.getInt(key, fallbackValue)

    override fun getLong(key: String, fallbackValue: Long) =
        sharedPreferences.getLong(key, fallbackValue)

    override fun getString(key: String, fallbackValue: String?): String? =
        sharedPreferences.getString(key, fallbackValue)

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

    private val sharedPreferences: SharedPreferences
        get() = application.getSharedPreferences(name, Context.MODE_PRIVATE)


    class BatchEditorImpl(
            private val sharedPreferencesEditor: SharedPreferences.Editor
    ) : BatchEditor {
        override fun apply() {
            sharedPreferencesEditor.apply()
        }

        override fun clear(): BatchEditor {
            sharedPreferencesEditor.clear()
            return this
        }

        override fun putInteger(key: String, value: Int): BatchEditor {
            sharedPreferencesEditor.putInt(key, value)
            return this
        }

        override fun putString(key: String, value: String): BatchEditor {
            sharedPreferencesEditor.putString(key, value)
            return this
        }
    }

}

package com.garpr.android.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.garpr.android.preferences.KeyValueStore.BatchEditor
import java.util.Collections

class KeyValueStoreImpl(
        private val context: Context,
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

    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(name, Context.MODE_PRIVATE)

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

    class BatchEditorImpl(
            private val editor: Editor
    ) : BatchEditor {

        override fun apply() {
            editor.apply()
        }

        override fun clear(): BatchEditor {
            editor.clear()
            return this
        }

        override fun putBoolean(key: String, value: Boolean): BatchEditor {
            editor.putBoolean(key, value)
            return this
        }

        override fun putInteger(key: String, value: Int): BatchEditor {
            editor.putInt(key, value)
            return this
        }

        override fun putString(key: String, value: String): BatchEditor {
            editor.putString(key, value)
            return this
        }

    }

}

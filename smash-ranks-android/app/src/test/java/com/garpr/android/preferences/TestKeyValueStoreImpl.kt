package com.garpr.android.preferences

import com.garpr.android.preferences.KeyValueStore.BatchEditor
import okhttp3.internal.toImmutableMap

class TestKeyValueStoreImpl : KeyValueStore {

    private val map = mutableMapOf<String, Any?>()

    override val all: Map<String, *>?
        get() = map.toImmutableMap()

    override fun batchEdit(): BatchEditor {
        return TestBatchEditorImpl(map)
    }

    override fun clear() {
        map.clear()
    }

    override fun contains(key: String): Boolean {
        return map.containsKey(key)
    }

    override fun getBoolean(key: String, fallbackValue: Boolean): Boolean {
        return map.getOrDefault(key, fallbackValue) as Boolean
    }

    override fun getFloat(key: String, fallbackValue: Float): Float {
        return map.getOrDefault(key, fallbackValue) as Float
    }

    override fun getInteger(key: String, fallbackValue: Int): Int {
        return map.getOrDefault(key, fallbackValue) as Int
    }

    override fun getLong(key: String, fallbackValue: Long): Long {
        return map.getOrDefault(key, fallbackValue) as Long
    }

    override fun getString(key: String, fallbackValue: String?): String? {
        return map.getOrDefault(key, fallbackValue) as String?
    }

    override fun remove(key: String) {
        map.remove(key)
    }

    override fun setBoolean(key: String, value: Boolean) {
        map[key] = value
    }

    override fun setFloat(key: String, value: Float) {
        map[key] = value
    }

    override fun setInteger(key: String, value: Int) {
        map[key] = value
    }

    override fun setLong(key: String, value: Long) {
        map[key] = value
    }

    override fun setString(key: String, value: String) {
        map[key] = value
    }

    class TestBatchEditorImpl(
            private val originalMap: MutableMap<String, Any?>
    ) : BatchEditor {

        private var clear = false
        private val map = mutableMapOf<String, Any?>()

        override fun apply() {
            if (clear) {
                originalMap.clear()
            }

            originalMap.putAll(map)
        }

        override fun clear(): BatchEditor {
            clear = true
            return this
        }

        override fun putBoolean(key: String, value: Boolean): BatchEditor {
            map[key] = value
            return this
        }

        override fun putInteger(key: String, value: Int): BatchEditor {
            map[key] = value
            return this
        }

        override fun putString(key: String, value: String): BatchEditor {
            map[key] = value
            return this
        }

    }

}

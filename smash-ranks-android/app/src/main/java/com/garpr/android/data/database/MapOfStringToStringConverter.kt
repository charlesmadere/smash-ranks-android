package com.garpr.android.data.database

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.koin.core.KoinComponent
import org.koin.core.get

class MapOfStringToStringConverter : KoinComponent {

    private val jsonAdapter by lazy {
        val moshi: Moshi = get()
        val type = Types.newParameterizedType(Map::class.java, String::class.java, String::class.java)
        val adapter: JsonAdapter<Map<String, String>> = moshi.adapter(type)
        adapter
    }

    @TypeConverter
    fun mapOfStringToStringFromString(string: String?): Map<String, String>? {
        return if (string.isNullOrEmpty()) {
            null
        } else {
            jsonAdapter.fromJson(string)
        }
    }

    @TypeConverter
    fun stringFromMapOfStringToString(map: Map<String, String>?): String? {
        return if (map.isNullOrEmpty()) {
            null
        } else {
            jsonAdapter.toJson(map)
        }
    }

}

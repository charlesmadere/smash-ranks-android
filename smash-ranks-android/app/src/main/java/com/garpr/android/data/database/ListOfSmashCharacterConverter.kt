package com.garpr.android.data.database

import androidx.room.TypeConverter
import com.garpr.android.data.models.SmashCharacter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.koin.core.KoinComponent
import org.koin.core.get

class ListOfSmashCharacterConverter : KoinComponent {

    private val jsonAdapter by lazy {
        val moshi: Moshi = get()
        val type = Types.newParameterizedType(List::class.java, SmashCharacter::class.java)
        val adapter: JsonAdapter<List<SmashCharacter?>> = moshi.adapter(type)
        adapter
    }

    @TypeConverter
    fun listOfSmashCharacterFromString(string: String?): List<SmashCharacter?>? {
        return if (string.isNullOrEmpty()) {
            null
        } else {
            jsonAdapter.fromJson(string)
        }
    }

    @TypeConverter
    fun stringFromListOfSmashCharacter(list: List<SmashCharacter?>?): String? {
        return if (list.isNullOrEmpty()) {
            null
        } else {
            jsonAdapter.toJson(list)
        }
    }

}

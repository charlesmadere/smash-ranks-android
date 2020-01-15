package com.garpr.android.data.database

import androidx.room.TypeConverter
import com.garpr.android.data.models.Region
import com.squareup.moshi.Moshi
import org.koin.core.KoinComponent
import org.koin.core.get

class RegionConverter : KoinComponent {

    private val jsonAdapter by lazy {
        val moshi: Moshi = get()
        moshi.adapter(Region::class.java)
    }

    @TypeConverter
    fun regionFromString(string: String?): Region? {
        return jsonAdapter.fromJson(string.orEmpty())
    }

    @TypeConverter
    fun stringFromRegion(region: Region?): String? {
        return jsonAdapter.toJson(region)
    }

}

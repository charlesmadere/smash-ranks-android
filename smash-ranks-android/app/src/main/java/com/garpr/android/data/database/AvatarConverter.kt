package com.garpr.android.data.database

import androidx.room.TypeConverter
import com.garpr.android.data.models.Avatar
import com.squareup.moshi.Moshi
import org.koin.core.KoinComponent
import org.koin.core.get

class AvatarConverter : KoinComponent {

    private val jsonAdapter by lazy {
        val moshi: Moshi = get()
        moshi.adapter(Avatar::class.java)
    }

    @TypeConverter
    fun avatarFromString(string: String?): Avatar? {
        return jsonAdapter.fromJson(string.orEmpty())
    }

    @TypeConverter
    fun stringFromAvatar(avatar: Avatar?): String? {
        return jsonAdapter.toJson(avatar)
    }

}

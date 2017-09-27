package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readStringMap
import com.garpr.android.extensions.writeStringMap

data class MeleeCharacterRoster(
        val players: Map<String, String>?,
        val regionId: String,
        val version: Int
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { MeleeCharacterRoster(it.readStringMap(), it.readString(),
                it.readInt()) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeStringMap(players)
        dest.writeString(regionId)
        dest.writeInt(version)
    }

}

package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel

data class MeleeCharacterRoster(
        val regionId: String,
        val version: Int
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { MeleeCharacterRoster(it.readString(), it.readInt()) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(regionId)
        dest.writeInt(version)
    }

}

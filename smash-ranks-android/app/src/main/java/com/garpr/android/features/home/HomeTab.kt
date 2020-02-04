package com.garpr.android.features.home

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel

enum class HomeTab : Parcelable {

    RANKINGS,
    TOURNAMENTS,
    FAVORITE_PLAYERS;

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }
    }

}

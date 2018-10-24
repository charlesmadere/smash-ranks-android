package com.garpr.android.misc

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel

enum class HomeTab : Parcelable {

    RANKINGS,
    TOURNAMENTS,
    FAVORITE_PLAYERS;

    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }

        fun from(index: Int): HomeTab {
            return values()[index]
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

}

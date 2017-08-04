package com.garpr.android.misc

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel

enum class ResultCodes : Parcelable {

    IDENTITY_SELECTED,
    REGION_SELECTED,
    RINGTONE_SELECTED;

    val mValue: Int = ordinal + 1000


    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

}

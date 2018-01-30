package com.garpr.android.misc

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel

enum class RequestCodes : Parcelable {

    CHANGE_REGION;


    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }
    }

    val value: Int
        get() = ordinal + 10000

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

}

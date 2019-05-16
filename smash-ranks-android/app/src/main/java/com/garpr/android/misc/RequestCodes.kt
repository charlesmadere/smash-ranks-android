package com.garpr.android.misc

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel

enum class RequestCodes : Parcelable {

    CHANGE_IDENTITY,
    CHANGE_REGION,
    CHANGE_RINGTONE;


    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }
    }

    val value: Int
        get() = ordinal + 10000

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

}

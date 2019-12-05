package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.squareup.moshi.Json

enum class PreviousRank : Parcelable {

    @Json(name = "decrease")
    DECREASE,

    @Json(name = "gone")
    GONE,

    @Json(name = "increase")
    INCREASE,

    @Json(name = "invisible")
    INVISIBLE;

    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

}

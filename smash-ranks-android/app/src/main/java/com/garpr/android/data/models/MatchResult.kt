package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.squareup.moshi.Json

enum class MatchResult : Parcelable {

    @Json(name = "excluded")
    EXCLUDED,

    @Json(name = "lose")
    LOSE,

    @Json(name = "win")
    WIN;

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }
    }

}

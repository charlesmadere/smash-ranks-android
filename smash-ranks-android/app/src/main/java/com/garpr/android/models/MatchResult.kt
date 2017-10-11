package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.google.gson.annotations.SerializedName

enum class MatchResult : Parcelable {

    @SerializedName("excluded")
    EXCLUDED,

    @SerializedName("lose")
    LOSE,

    @SerializedName("win")
    WIN;


    companion object {
        @JvmField
        val CREATOR = createParcel { MatchResult.values()[it.readInt()] }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

}

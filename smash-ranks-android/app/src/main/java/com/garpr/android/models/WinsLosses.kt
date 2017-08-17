package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel

import com.google.gson.annotations.SerializedName

data class WinsLosses(
        @SerializedName("wins") val wins: Int,
        @SerializedName("losses") val losses: Int
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { WinsLosses(it.readInt(), it.readInt()) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(wins)
        dest.writeInt(losses)
    }

}

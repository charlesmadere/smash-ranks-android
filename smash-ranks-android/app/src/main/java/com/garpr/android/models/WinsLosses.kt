package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel

import com.google.gson.annotations.SerializedName

class WinsLosses(wins: Int, losses: Int) : Parcelable {

    @SerializedName("losses")
    val mLosses: Int = losses

    @SerializedName("wins")
    val mWins: Int = wins


    companion object {
        @JvmField
        val CREATOR = createParcel { WinsLosses(it.readInt(), it.readInt()) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(mWins)
        dest.writeInt(mLosses)
    }

}

package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

abstract class AbsMatch(
        @SerializedName("result") val result: MatchResult
) : Parcelable {

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(result, flags)
    }

}

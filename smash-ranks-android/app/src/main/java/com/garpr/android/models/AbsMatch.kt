package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.misc.MiscUtils
import com.google.gson.annotations.SerializedName

abstract class AbsMatch(
        @SerializedName("result") val result: MatchResult
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        return other is AbsMatch && result == other.result
    }

    override fun hashCode() = MiscUtils.hashCode(result)

    override fun toString() = result.toString()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(result, flags)
    }

}

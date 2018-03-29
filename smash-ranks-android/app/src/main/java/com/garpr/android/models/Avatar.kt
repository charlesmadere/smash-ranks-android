package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.google.gson.annotations.SerializedName

data class Avatar(
        @SerializedName("large") val large: String? = null,
        @SerializedName("medium") val medium: String? = null,
        @SerializedName("small") val small: String? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { Avatar(it.readString(), it.readString(), it.readString()) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(large)
        dest.writeString(medium)
        dest.writeString(small)
    }

}

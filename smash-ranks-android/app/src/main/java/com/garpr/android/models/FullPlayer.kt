package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readRatingsMap
import com.garpr.android.extensions.writeRatingsMap
import com.google.gson.annotations.SerializedName

class FullPlayer(
        id: String,
        name: String,
        @SerializedName("aliases") val aliases: List<String>? = null,
        @SerializedName("regions") val regions: List<String>? = null,
        @SerializedName("ratings") val ratings: Map<String, Rating>? = null
) : AbsPlayer(
        id,
        name
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { FullPlayer(it.readString(), it.readString(),
                it.createStringArrayList(), it.createStringArrayList(), it.readRatingsMap()) }
    }

    override val kind
        get() = Kind.FULL

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeStringList(aliases)
        dest.writeStringList(regions)
        dest.writeRatingsMap(ratings)
    }

}

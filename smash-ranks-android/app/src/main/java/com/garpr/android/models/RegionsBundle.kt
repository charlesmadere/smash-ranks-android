package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.google.gson.annotations.SerializedName

data class RegionsBundle(
        @SerializedName("regions") val regions: List<LiteRegion> = mutableListOf()
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { RegionsBundle(it.createTypedArrayList(LiteRegion.CREATOR)) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(regions)
    }

}

package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readAbsRegionList
import com.garpr.android.extensions.writeAbsRegionList
import com.google.gson.annotations.SerializedName

data class RegionsBundle(
        @SerializedName("regions") val regions: List<AbsRegion>? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { RegionsBundle(it.readAbsRegionList()) }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeAbsRegionList(regions, flags)
    }

}

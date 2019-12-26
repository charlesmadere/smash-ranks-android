package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.optAbsRegionList
import com.garpr.android.extensions.writeAbsRegionList
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegionsBundle(
        @Json(name = "regions") val regions: List<AbsRegion>? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            RegionsBundle(
                    it.optAbsRegionList()
            )
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeAbsRegionList(regions, flags)
    }

}

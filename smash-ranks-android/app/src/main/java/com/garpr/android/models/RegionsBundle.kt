package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.google.gson.annotations.SerializedName
import java.util.*

class RegionsBundle : Parcelable {

    @SerializedName("regions")
    var regions: ArrayList<Region>? = null
        private set


    companion object {
        @JvmField
        val CREATOR = createParcel {
            val rb = RegionsBundle()
            rb.regions = it.createTypedArrayList(Region.CREATOR)
            rb
        }
    }

    fun merge(regionsBundle: RegionsBundle?) {
        val regions = regionsBundle?.regions

        if (regions == null || regions.isEmpty()) {
            return
        }

        synchronized (this) {
            var _regions = this.regions

            if (_regions == null) {
                _regions = ArrayList()
            }

            for (region in regions) {
                if (region !in _regions) {
                    _regions.add(region)
                }
            }

            Collections.sort(_regions, Region.ALPHABETICAL_ORDER)
            this.regions = _regions
        }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(regions)
    }

}

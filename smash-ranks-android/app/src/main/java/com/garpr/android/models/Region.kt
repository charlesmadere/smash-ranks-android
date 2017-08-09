package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readInteger
import com.google.gson.annotations.SerializedName

class Region(
        rankingActivityDayLimit: Int? = null,
        rankingNumTourneysAttended: Int? = null,
        tournamentQualifiedDayLimit: Int? = null,
        displayName: String,
        id: String,
        @SerializedName("endpoint") val endpoint: Endpoint
) : AbsRegion(
        rankingActivityDayLimit,
        rankingNumTourneysAttended,
        tournamentQualifiedDayLimit,
        displayName,
        id
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { Region(it.readInteger(), it.readInteger(), it.readInteger(),
                it.readString(), it.readString(), it.readParcelable(Endpoint::class.java.classLoader)) }
    }

    constructor(region: AbsRegion, endpoint: Endpoint) : this(region.rankingActivityDayLimit,
            region.rankingNumTourneysAttended, region.tournamentQualifiedDayLimit,
            region.displayName, region.id, endpoint)

    override val kind
        get() = Kind.FULL

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeParcelable(endpoint, flags)
    }

}

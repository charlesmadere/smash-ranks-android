package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readBoolean
import com.garpr.android.extensions.readInteger
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.requireString
import com.google.gson.annotations.SerializedName

class Region(
        activeTf: Boolean? = null,
        rankingActivityDayLimit: Int? = null,
        rankingNumTourneysAttended: Int? = null,
        tournamentQualifiedDayLimit: Int? = null,
        displayName: String,
        id: String,
        @SerializedName("endpoint") val endpoint: Endpoint
) : AbsRegion(
        activeTf,
        rankingActivityDayLimit,
        rankingNumTourneysAttended,
        tournamentQualifiedDayLimit,
        displayName,
        id
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            Region(
                    it.readBoolean(),
                    it.readInteger(),
                    it.readInteger(),
                    it.readInteger(),
                    it.requireString(),
                    it.requireString(),
                    it.requireParcelable(Endpoint::class.java.classLoader)
            )
        }
    }

    constructor(region: AbsRegion, endpoint: Endpoint) : this(region.activeTf,
            region.rankingActivityDayLimit, region.rankingNumTourneysAttended,
            region.tournamentQualifiedDayLimit, region.displayName, region.id, endpoint)

    override fun equals(other: Any?): Boolean {
        return super.equals(other) && other is Region && endpoint == other.endpoint
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + endpoint.hashCode()
        return result
    }

    override val kind
        get() = Kind.FULL

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeParcelable(endpoint, flags)
    }

}

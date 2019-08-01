package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.core.util.ObjectsCompat
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readBoolean
import com.garpr.android.extensions.readInteger
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.requireString
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Region(
        @Json(name = "activeTF") activeTf: Boolean? = null,
        @Json(name = "ranking_activity_day_limit") rankingActivityDayLimit: Int? = null,
        @Json(name = "ranking_num_tourneys_attended") rankingNumTourneysAttended: Int? = null,
        @Json(name = "tournament_qualified_day_limit") tournamentQualifiedDayLimit: Int? = null,
        @Json(name = "display_name") displayName: String,
        @Json(name = "id") id: String,
        @Json(name = "endpoint") val endpoint: Endpoint
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

    override fun hashCode(): Int = ObjectsCompat.hash(id, endpoint)

    override val kind: Kind
        get() = Kind.FULL

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeParcelable(endpoint, flags)
    }

}

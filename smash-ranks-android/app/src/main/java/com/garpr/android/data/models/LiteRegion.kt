package com.garpr.android.data.models

import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.optBoolean
import com.garpr.android.extensions.optInteger
import com.garpr.android.extensions.requireString
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class LiteRegion(
        @Json(name = "activeTF") activeTf: Boolean? = null,
        @Json(name = "ranking_activity_day_limit") rankingActivityDayLimit: Int? = null,
        @Json(name = "ranking_num_tourneys_attended") rankingNumTourneysAttended: Int? = null,
        @Json(name = "tournament_qualified_day_limit") tournamentQualifiedDayLimit: Int? = null,
        @Json(name = "display_name") displayName: String,
        @Json(name = "id") id: String
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
            LiteRegion(
                    it.optBoolean(),
                    it.optInteger(),
                    it.optInteger(),
                    it.optInteger(),
                    it.requireString(),
                    it.requireString()
            )
        }
    }

    override val kind: Kind
        get() = Kind.LITE

}

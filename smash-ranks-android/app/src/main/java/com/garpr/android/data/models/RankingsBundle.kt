package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.requireAbsRegion
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.requireString
import com.garpr.android.extensions.writeAbsRegion
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RankingsBundle(
        @Json(name = "ranking_criteria") val rankingCriteria: AbsRegion,
        @Json(name = "ranking") val rankings: List<RankedPlayer>? = null,
        @Json(name = "tournaments") val tournaments: List<String>? = null,
        @Json(name = "time") val time: SimpleDate,
        @Json(name = "id") val id: String,
        @Json(name = "region") val region: String
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            RankingsBundle(
                    it.requireAbsRegion(),
                    it.createTypedArrayList(RankedPlayer.CREATOR),
                    it.createStringArrayList(),
                    it.requireParcelable(SimpleDate::class.java.classLoader),
                    it.requireString(),
                    it.requireString()
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is RankingsBundle && id.equals(other.id, ignoreCase = true)
    }

    override fun hashCode(): Int = id.hashCode()

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeAbsRegion(rankingCriteria, flags)
        dest.writeTypedList(rankings)
        dest.writeStringList(tournaments)
        dest.writeParcelable(time, flags)
        dest.writeString(id)
        dest.writeString(region)
    }

}

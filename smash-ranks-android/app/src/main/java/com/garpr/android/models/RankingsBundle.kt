package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readAbsRegion
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.requireString
import com.garpr.android.extensions.writeAbsRegion
import com.google.gson.annotations.SerializedName

data class RankingsBundle(
        @SerializedName("ranking") val rankings: List<RankedPlayer>? = null,
        @SerializedName("tournaments") val tournaments: List<String>? = null,
        @SerializedName("ranking_criteria") val rankingCriteria: AbsRegion,
        @SerializedName("time") val time: SimpleDate,
        @SerializedName("id") val id: String,
        @SerializedName("region") val region: String
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            RankingsBundle(
                    it.createTypedArrayList(RankedPlayer.CREATOR),
                    it.createStringArrayList(),
                    it.readAbsRegion(),
                    it.requireParcelable(SimpleDate::class.java.classLoader),
                    it.requireString(),
                    it.requireString()
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is RankingsBundle && id.equals(other.id, ignoreCase = true)
    }

    override fun hashCode() = id.hashCode()

    fun hasPreviousRank(): Boolean {
        return rankings?.asSequence()
                ?.firstOrNull { it.previousRank != null } != null
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(rankings)
        dest.writeStringList(tournaments)
        dest.writeAbsRegion(rankingCriteria, flags)
        dest.writeParcelable(time, flags)
        dest.writeString(id)
        dest.writeString(region)
    }

}

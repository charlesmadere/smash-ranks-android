package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readRankingCriteria
import com.google.gson.annotations.SerializedName

data class RankingsBundle(
        @SerializedName("ranking") val rankings: List<RankedPlayer>? = null,
        @SerializedName("tournaments") val tournaments: List<String>? = null,
        @SerializedName("ranking_criteria") val rankingCriteria: RankingCriteria,
        @SerializedName("time") val time: SimpleDate,
        @SerializedName("id") val id: String,
        @SerializedName("region") val region: String
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { RankingsBundle(it.createTypedArrayList(RankedPlayer.CREATOR),
                it.createStringArrayList(), it.readRankingCriteria(),
                it.readParcelable(SimpleDate::class.java.classLoader), it.readString(),
                it.readString()) }
    }

    override fun equals(other: Any?): Boolean {
        return other is RankingsBundle && id.equals(other.id, ignoreCase = true)
    }

    override fun hashCode() = id.hashCode()

    fun hasPreviousRank(): Boolean {
        rankings?.filter { it.previousRank != null }
                ?.forEach { return true }

        return false
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(rankings)
        dest.writeStringList(tournaments)
        dest.writeParcelable(time, flags)
        dest.writeString(id)
        dest.writeString(region)
    }

}

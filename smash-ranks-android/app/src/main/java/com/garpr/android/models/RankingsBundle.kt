package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel

import com.google.gson.annotations.SerializedName

data class RankingsBundle(
        @SerializedName("ranking") val rankings: List<Ranking>? = null,
        @SerializedName("tournaments") val tournaments: List<String>? = null,
        @SerializedName("time") val time: SimpleDate,
        @SerializedName("id") val id: String,
        @SerializedName("region") val region: String
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { RankingsBundle(it.createTypedArrayList(Ranking.CREATOR),
                it.createStringArrayList(), it.readParcelable(SimpleDate::class.java.classLoader),
                it.readString(), it.readString()) }
    }

    override fun equals(other: Any?): Boolean {
        return other is RankingsBundle && id == other.id
    }

    override fun hashCode() = id.hashCode()

    fun hasPreviousRank(): Boolean {
        if (rankings?.isNotEmpty() == true) {
            for (ranking in rankings) {
                if (ranking.previousRank != null) {
                    return true
                }
            }
        }

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

package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.misc.ParcelableUtils
import com.google.gson.annotations.SerializedName
import java.util.*

data class Region(
        @SerializedName("endpoint") val endpoint: Endpoint,
        @SerializedName("ranking_activity_day_limit") val rankingActivityDayLimit: Int?,
        @SerializedName("ranking_num_tourneys_attended") val rankingNumTourneysAttended: Int?,
        @SerializedName("tournament_qualified_day_limit") val tournamentQualifiedDayLimit: Int?,
        @SerializedName("display_name") val displayName: String,
        @SerializedName("id") val id: String
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        return other is Region && id.equals(other.id, ignoreCase = true)
    }

    fun hasActivityRequirements(): Boolean {
        return rankingActivityDayLimit != null && rankingNumTourneysAttended != null
    }

    override fun hashCode() = id.hashCode()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(endpoint, flags)
        ParcelableUtils.writeInteger(rankingActivityDayLimit, dest)
        ParcelableUtils.writeInteger(rankingNumTourneysAttended, dest)
        ParcelableUtils.writeInteger(tournamentQualifiedDayLimit, dest)
        dest.writeString(displayName)
        dest.writeString(id)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel { Region(it.readParcelable(Endpoint::class.java.classLoader),
                ParcelableUtils.readInteger(it), ParcelableUtils.readInteger(it),
                ParcelableUtils.readInteger(it), it.readString(), it.readString()) }

        val ALPHABETICAL_ORDER: Comparator<Region> = Comparator { o1, o2 ->
            o1.displayName.compareTo(o2.displayName, ignoreCase = true)
        }
    }

}

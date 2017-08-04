package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readInteger
import com.garpr.android.extensions.writeInteger
import com.google.gson.annotations.SerializedName

data class Region(
        @SerializedName("endpoint") val endpoint: Endpoint,
        @SerializedName("ranking_activity_day_limit") val rankingActivityDayLimit: Int? = null,
        @SerializedName("ranking_num_tourneys_attended") val rankingNumTourneysAttended: Int? = null,
        @SerializedName("tournament_qualified_day_limit") val tournamentQualifiedDayLimit: Int? = null,
        @SerializedName("display_name") val displayName: String,
        @SerializedName("id") val id: String
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { Region(it.readParcelable(Endpoint::class.java.classLoader),
                it.readInteger(), it.readInteger(), it.readInteger(), it.readString(),
                it.readString()) }
    }

    override fun equals(other: Any?): Boolean {
        return other is Region && id.equals(other.id, ignoreCase = true)
    }

    fun hasActivityRequirements() = rankingActivityDayLimit != null &&
            rankingNumTourneysAttended != null

    override fun hashCode() = id.hashCode()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(endpoint, flags)
        dest.writeInteger(rankingActivityDayLimit)
        dest.writeInteger(rankingNumTourneysAttended)
        dest.writeInteger(tournamentQualifiedDayLimit)
        dest.writeString(displayName)
        dest.writeString(id)
    }

}

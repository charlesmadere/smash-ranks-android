package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.writeInteger
import com.google.gson.annotations.SerializedName
import java.util.*

abstract class AbsRegion(
        @SerializedName("ranking_activity_day_limit") val rankingActivityDayLimit: Int? = null,
        @SerializedName("ranking_num_tourneys_attended") val rankingNumTourneysAttended: Int? = null,
        @SerializedName("tournament_qualified_day_limit") val tournamentQualifiedDayLimit: Int? = null,
        @SerializedName("display_name") val displayName: String,
        @SerializedName("id") val id: String
) : Parcelable {

    companion object {
        val ALPHABETICAL_ORDER: Comparator<AbsRegion> = Comparator { o1, o2 ->
            o1.displayName.compareTo(o2.displayName, ignoreCase = true)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is AbsRegion && id.equals(other.id, ignoreCase = true)
    }

    fun hasActivityRequirements(): Boolean {
        return rankingActivityDayLimit != null && rankingNumTourneysAttended != null
    }

    override fun hashCode() = id.hashCode()

    abstract val kind: Kind

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInteger(rankingActivityDayLimit)
        dest.writeInteger(rankingNumTourneysAttended)
        dest.writeInteger(tournamentQualifiedDayLimit)
        dest.writeString(displayName)
        dest.writeString(id)
    }


    enum class Kind : Parcelable {
        @SerializedName("full")
        FULL,

        @SerializedName("lite")
        LITE;

        companion object {
            @JvmField
            val CREATOR = createParcel { values()[it.readInt()] }
        }

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(ordinal)
        }
    }

}

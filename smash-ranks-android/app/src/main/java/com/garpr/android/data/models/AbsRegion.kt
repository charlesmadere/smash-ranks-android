package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.writeBoolean
import com.garpr.android.extensions.writeInteger
import com.squareup.moshi.Json

abstract class AbsRegion(
        val activeTf: Boolean? = null,
        val rankingActivityDayLimit: Int? = null,
        val rankingNumTourneysAttended: Int? = null,
        val tournamentQualifiedDayLimit: Int? = null,
        val displayName: String,
        val id: String
) : Parcelable {

    val hasActivityRequirements: Boolean
        get() = rankingActivityDayLimit != null && rankingNumTourneysAttended != null

    val isActive: Boolean
        get() = activeTf ?: true

    override fun equals(other: Any?): Boolean {
        return other is AbsRegion && id.equals(other.id, ignoreCase = true)
    }

    override fun hashCode(): Int = id.hashCode()

    abstract val kind: Kind

    override fun toString(): String = displayName

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeBoolean(activeTf)
        dest.writeInteger(rankingActivityDayLimit)
        dest.writeInteger(rankingNumTourneysAttended)
        dest.writeInteger(tournamentQualifiedDayLimit)
        dest.writeString(displayName)
        dest.writeString(id)
    }

    companion object {
        val ALPHABETICAL_ORDER = Comparator<AbsRegion> { o1, o2 ->
            o1.displayName.compareTo(o2.displayName, ignoreCase = true)
        }

        val ENDPOINT_ORDER = Comparator<AbsRegion> { o1, o2 ->
            var result = 0

            if (o1 is Region && o2 is Region) {
                result = Endpoint.ALPHABETICAL_ORDER.compare(o1.endpoint, o2.endpoint)
            } else if (o1 is Region) {
                result = -1
            } else if (o2 is Region) {
                result = 1
            }

            if (result == 0) {
                result = ALPHABETICAL_ORDER.compare(o1, o2)
            }

            result
        }
    }

    enum class Kind : Parcelable {

        @Json(name = "full")
        FULL,

        @Json(name = "lite")
        LITE;

        override fun describeContents(): Int = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(ordinal)
        }

        companion object {
            @JvmField
            val CREATOR = createParcel { values()[it.readInt()] }
        }

    }

}

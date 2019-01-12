package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.writeBoolean
import com.garpr.android.extensions.writeInteger
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import com.google.gson.annotations.SerializedName
import java.util.Comparator

abstract class AbsRegion(
        @SerializedName("activeTF") val activeTf: Boolean? = null,
        @SerializedName("ranking_activity_day_limit") override val rankingActivityDayLimit: Int? = null,
        @SerializedName("ranking_num_tourneys_attended") override val rankingNumTourneysAttended: Int? = null,
        @SerializedName("tournament_qualified_day_limit") override val tournamentQualifiedDayLimit: Int? = null,
        @SerializedName("display_name") val displayName: String,
        @SerializedName("id") val id: String
) : Parcelable, RankingCriteria {

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

        val JSON_DESERIALIZER = JsonDeserializer<AbsRegion> { json, typeOfT, context ->
            if (json == null || json.isJsonNull) {
                return@JsonDeserializer null
            }

            val jsonObject = json.asJsonObject

            if (jsonObject.has("endpoint")) {
                context.deserialize<Region>(json, Region::class.java)
            } else {
                context.deserialize<LiteRegion>(json, LiteRegion::class.java)
            }
        }

        val JSON_SERIALIZER = JsonSerializer<AbsRegion> { src, typeOfSrc, context ->
            when (src?.kind) {
                Kind.FULL -> context.serialize(src, Region::class.java)
                Kind.LITE -> context.serialize(src, LiteRegion::class.java)
                else -> null
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is AbsRegion && id.equals(other.id, ignoreCase = true)
    }

    val hasActivityRequirements
        get() = rankingActivityDayLimit != null && rankingNumTourneysAttended != null

    override fun hashCode(): Int = id.hashCode()

    override val isActive: Boolean
        get() = activeTf ?: true

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


    enum class Kind : Parcelable {
        @SerializedName("full")
        FULL,

        @SerializedName("lite")
        LITE;

        companion object {
            @JvmField
            val CREATOR = createParcel { values()[it.readInt()] }
        }

        override fun describeContents(): Int = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(ordinal)
        }
    }

}

package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.writeInteger
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
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

        val JSON_DESERIALIZER: JsonDeserializer<AbsRegion> = JsonDeserializer<AbsRegion> { json, typeOfT, context ->
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

        val JSON_SERIALIZER: JsonSerializer<AbsRegion> = JsonSerializer { src, typeOfSrc, context ->
            if (src == null) {
                return@JsonSerializer null
            }

            when (src.kind) {
                Kind.FULL -> return@JsonSerializer context.serialize(src, Region::class.java)
                Kind.LITE -> return@JsonSerializer context.serialize(src, LitePlayer::class.java)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is AbsRegion && id.equals(other.id, ignoreCase = true)
    }

    val hasActivityRequirements
        get() = rankingActivityDayLimit != null && rankingNumTourneysAttended != null

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

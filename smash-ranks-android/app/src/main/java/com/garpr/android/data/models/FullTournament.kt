package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.ParcelCompat
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.optAbsPlayerList
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.requireString
import com.garpr.android.extensions.writeAbsPlayerList
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class FullTournament(
        @Json(name = "regions") regions: List<String>? = null,
        @Json(name = "date") date: SimpleDate,
        @Json(name = "id") id: String,
        @Json(name = "name") name: String,
        @Json(name = "players") val players: List<AbsPlayer>? = null,
        @Json(name = "matches") val matches: List<Match>? = null,
        @Json(name = "rawId") val rawId: String? = null,
        @Json(name = "url") val url: String? = null
) : AbsTournament(
        regions,
        date,
        id,
        name
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            FullTournament(
                    it.createStringArrayList(),
                    it.requireParcelable(SimpleDate::class.java.classLoader),
                    it.requireString(),
                    it.requireString(),
                    it.optAbsPlayerList(),
                    it.createTypedArrayList(Match.CREATOR),
                    it.readString(),
                    it.readString()
            )
        }
    }

    override val kind: Kind
        get() = Kind.FULL

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeAbsPlayerList(players, flags)
        dest.writeTypedList(matches)
        dest.writeString(rawId)
        dest.writeString(url)
    }

    @JsonClass(generateAdapter = true)
    data class Match(
            @Json(name = "excluded") val isExcluded: Boolean,
            @Json(name = "loser_id") val loserId: String,
            @Json(name = "loser_name") val loserName: String,
            @Json(name = "match_id") val matchId: String,
            @Json(name = "winner_id") val winnerId: String,
            @Json(name = "winner_name") val winnerName: String
    ) : Parcelable {
        companion object {
            @JvmField
            val CREATOR = createParcel {
                Match(
                        ParcelCompat.readBoolean(it),
                        it.requireString(),
                        it.requireString(),
                        it.requireString(),
                        it.requireString(),
                        it.requireString()
                )
            }
        }

        override fun equals(other: Any?): Boolean {
            return other is Match && matchId.equals(other.matchId, ignoreCase = true)
        }

        override fun hashCode(): Int = matchId.hashCode()

        override fun describeContents(): Int = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            ParcelCompat.writeBoolean(dest, isExcluded)
            dest.writeString(loserId)
            dest.writeString(loserName)
            dest.writeString(matchId)
            dest.writeString(winnerId)
            dest.writeString(winnerName)
        }
    }

}

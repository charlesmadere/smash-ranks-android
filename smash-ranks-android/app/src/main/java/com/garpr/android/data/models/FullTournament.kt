package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.ParcelCompat
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.optAbsPlayerList
import com.garpr.android.extensions.requireAbsPlayer
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.requireString
import com.garpr.android.extensions.writeAbsPlayer
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
        @Json(name = "url") val url: String? = null
) : AbsTournament(
        regions,
        date,
        id,
        name
), Parcelable {

    override val kind: Kind
        get() = Kind.FULL

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeAbsPlayerList(players, flags)
        dest.writeTypedList(matches)
        dest.writeString(url)
    }

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
                    it.readString()
            )
        }
    }

    @JsonClass(generateAdapter = false)
    data class Match(
            val loser: AbsPlayer,
            val winner: AbsPlayer,
            val excluded: Boolean,
            val matchId: Int
    ) : Parcelable {

        override fun equals(other: Any?): Boolean {
            return other is Match && matchId == other.matchId
        }

        override fun hashCode(): Int = matchId.hashCode()

        override fun describeContents(): Int = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeAbsPlayer(loser, flags)
            dest.writeAbsPlayer(winner, flags)
            ParcelCompat.writeBoolean(dest, excluded)
            dest.writeInt(matchId)
        }

        companion object {
            @JvmField
            val CREATOR = createParcel {
                Match(
                        it.requireAbsPlayer(),
                        it.requireAbsPlayer(),
                        ParcelCompat.readBoolean(it),
                        it.readInt()
                )
            }
        }
    }

}

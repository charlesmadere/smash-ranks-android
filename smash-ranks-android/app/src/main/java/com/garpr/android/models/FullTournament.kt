package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readAbsPlayerList
import com.garpr.android.extensions.readBoolean
import com.garpr.android.extensions.writeAbsPlayerList
import com.garpr.android.extensions.writeBoolean
import com.google.gson.annotations.SerializedName

class FullTournament(
        regions: List<String>? = null,
        date: SimpleDate,
        id: String,
        name: String,
        @SerializedName("players") val players: List<AbsPlayer>? = null,
        @SerializedName("matches") val matches: List<Match>? = null,
        @SerializedName("rawId") val rawId: String? = null,
        @SerializedName("url") val url: String? = null
) : AbsTournament(
        regions,
        date,
        id,
        name
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { FullTournament(it.createStringArrayList(),
                it.readParcelable(SimpleDate::class.java.classLoader), it.readString(),
                it.readString(), it.readAbsPlayerList(),
                it.createTypedArrayList(FullTournament.Match.CREATOR), it.readString(),
                it.readString()) }
    }

    override val kind
        get() = Kind.FULL

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeAbsPlayerList(players, flags)
        dest.writeTypedList(matches)
        dest.writeString(rawId)
        dest.writeString(url)
    }


    data class Match(
            @SerializedName("excluded") val isExcluded: Boolean,
            @SerializedName("loser_id") val loserId: String,
            @SerializedName("loser_name") val loserName: String,
            @SerializedName("match_id") val matchId: String,
            @SerializedName("winner_id") val winnerId: String,
            @SerializedName("winner_name") val winnerName: String
    ) : Parcelable {
        companion object {
            @JvmField
            val CREATOR = createParcel { Match(it.readBoolean(), it.readString(), it.readString(),
                    it.readString(), it.readString(), it.readString()) }
        }

        override fun equals(other: Any?): Boolean {
            return other is Match && matchId.equals(other.matchId, ignoreCase = true)
        }

        override fun hashCode() = matchId.hashCode()

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeBoolean(isExcluded)
            dest.writeString(loserId)
            dest.writeString(loserName)
            dest.writeString(matchId)
            dest.writeString(winnerId)
            dest.writeString(winnerName)
        }
    }

}

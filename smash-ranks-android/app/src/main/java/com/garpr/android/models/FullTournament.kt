package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.misc.ParcelableUtils
import com.google.gson.annotations.SerializedName
import java.util.*

class FullTournament : AbsTournament(), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            val ft = FullTournament()
            ft.readFromParcel(it)
            ft
        }
    }

    @SerializedName("players")
    var players: ArrayList<AbsPlayer>? = null
        private set

    @SerializedName("matches")
    var matches: ArrayList<Match>? = null
        private set

    @SerializedName("raw_id")
    var rawId: String? = null
        private set

    @SerializedName("url")
    var url: String? = null
        private set


    override val kind = AbsTournament.Kind.FULL

    override fun readFromParcel(source: Parcel) {
        super.readFromParcel(source)
        players = ParcelableUtils.readAbsPlayerList(source)
        matches = source.createTypedArrayList(Match.CREATOR)
        rawId = source.readString()
        url = source.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        ParcelableUtils.writeAbsPlayerList(players, dest, flags)
        dest.writeTypedList(matches)
        dest.writeString(rawId)
        dest.writeString(url)
    }


    class Match : Parcelable {
        companion object {
            @JvmField
            val CREATOR = createParcel {
                val m = Match()
                m.isExcluded = it.readInt() != 0
                m.loserId = it.readString()
                m.loserName = it.readString()
                m.matchId = it.readString()
                m.winnerId = it.readString()
                m.winnerName = it.readString()
                m
            }
        }

        @SerializedName("excluded")
        var isExcluded: Boolean = false
            private set

        @SerializedName("loser_id")
        lateinit var loserId: String
            private set

        @SerializedName("loser_name")
        lateinit var loserName: String
            private set

        @SerializedName("match_id")
        lateinit var matchId: String
            private set

        @SerializedName("winner_id")
        lateinit var winnerId: String
            private set

        @SerializedName("winner_name")
        lateinit var winnerName: String
            private set

        override fun equals(other: Any?): Boolean {
            return other is Match && matchId == other.matchId
        }

        override fun hashCode() = matchId.hashCode()

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(if (isExcluded) 1 else 0)
            dest.writeString(loserId)
            dest.writeString(loserName)
            dest.writeString(matchId)
            dest.writeString(winnerId)
            dest.writeString(winnerName)
        }
    }

}

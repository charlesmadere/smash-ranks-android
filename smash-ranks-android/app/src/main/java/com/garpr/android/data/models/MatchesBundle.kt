package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.requireAbsPlayer
import com.garpr.android.extensions.writeAbsPlayer
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchesBundle(
        @Json(name = "player") val player: AbsPlayer,
        @Json(name = "losses") val losses: Int = 0,
        @Json(name = "wins") val wins: Int = 0,
        @Json(name = "matches") val matches: List<TournamentMatch>? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            MatchesBundle(
                    it.requireAbsPlayer(),
                    it.readInt(),
                    it.readInt(),
                    it.createTypedArrayList(TournamentMatch.CREATOR)
            )
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeAbsPlayer(player, flags)
        dest.writeInt(losses)
        dest.writeInt(wins)
        dest.writeTypedList(matches)
    }

}

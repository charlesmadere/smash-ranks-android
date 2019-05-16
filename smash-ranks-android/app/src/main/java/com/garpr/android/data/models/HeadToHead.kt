package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readAbsPlayer
import com.garpr.android.extensions.writeAbsPlayer
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HeadToHead(
        @Json(name = "opponent") val opponent: AbsPlayer,
        @Json(name = "player") val player: AbsPlayer,
        @Json(name = "losses") val losses: Int = 0,
        @Json(name = "wins") val wins: Int = 0,
        @Json(name = "matches") val matches: List<Match>? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            HeadToHead(
                    it.readAbsPlayer(),
                    it.readAbsPlayer(),
                    it.readInt(),
                    it.readInt(),
                    it.createTypedArrayList(Match.CREATOR)
            )
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeAbsPlayer(opponent, flags)
        dest.writeAbsPlayer(player, flags)
        dest.writeInt(losses)
        dest.writeInt(wins)
        dest.writeTypedList(matches)
    }

}

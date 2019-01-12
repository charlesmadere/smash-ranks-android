package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readAbsPlayer
import com.garpr.android.extensions.writeAbsPlayer
import com.google.gson.annotations.SerializedName

data class MatchesBundle(
        @SerializedName("player") val player: AbsPlayer,
        @SerializedName("losses") val losses: Int = 0,
        @SerializedName("wins") val wins: Int = 0,
        @SerializedName("matches") val matches: List<Match>? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { MatchesBundle(it.readAbsPlayer(), it.readInt(),
                it.readInt(), it.createTypedArrayList(Match.CREATOR)) }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeAbsPlayer(player, flags)
        dest.writeInt(losses)
        dest.writeInt(wins)
        dest.writeTypedList(matches)
    }

}

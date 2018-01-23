package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readAbsPlayer
import com.garpr.android.extensions.writeAbsPlayer
import com.garpr.android.misc.MiscUtils
import com.google.gson.annotations.SerializedName

class HeadToHeadMatch(
        result: MatchResult,
        @SerializedName("player") val player: AbsPlayer,
        @SerializedName("opponent") val opponent: AbsPlayer
) : AbsMatch(
        result
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { HeadToHeadMatch(it.readParcelable(MatchResult::class.java.classLoader),
                it.readAbsPlayer(), it.readAbsPlayer()) }
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other) && other is HeadToHeadMatch && player == other.player
                && opponent == other.opponent
    }

    override fun hashCode(): Int {
        return MiscUtils.hash(result, player, opponent)
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeAbsPlayer(player, flags)
        dest.writeAbsPlayer(opponent, flags)
    }

}

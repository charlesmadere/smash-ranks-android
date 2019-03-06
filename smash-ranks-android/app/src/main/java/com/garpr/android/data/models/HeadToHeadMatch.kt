package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readAbsPlayer
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.writeAbsPlayer
import com.garpr.android.misc.MiscUtils
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class HeadToHeadMatch(
        @Json(name = "result") result: MatchResult,
        @Json(name = "player") val player: AbsPlayer,
        @Json(name = "opponent") val opponent: AbsPlayer
) : AbsMatch(
        result
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            HeadToHeadMatch(
                    it.requireParcelable(MatchResult::class.java.classLoader),
                    it.readAbsPlayer(),
                    it.readAbsPlayer()
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other) && other is HeadToHeadMatch && player == other.player
                && opponent == other.opponent
    }

    override fun hashCode(): Int = MiscUtils.hashCode(result, player, opponent)

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeAbsPlayer(player, flags)
        dest.writeAbsPlayer(opponent, flags)
    }

}
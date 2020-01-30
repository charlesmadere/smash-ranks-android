package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.requireAbsPlayer
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.writeAbsPlayer
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Objects

@JsonClass(generateAdapter = true)
class HeadToHeadMatch(
        @Json(name = "player") val player: AbsPlayer,
        @Json(name = "opponent") val opponent: AbsPlayer,
        @Json(name = "result") val result: MatchResult
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        return if (other is HeadToHeadMatch) {
            player == other.player && opponent == other.opponent && result == other.result
        } else {
            false
        }
    }

    override fun hashCode(): Int = Objects.hash(player, opponent, result)

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeAbsPlayer(player, flags)
        dest.writeAbsPlayer(opponent, flags)
        dest.writeParcelable(result, flags)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel {
            HeadToHeadMatch(
                    it.requireAbsPlayer(),
                    it.requireAbsPlayer(),
                    it.requireParcelable(MatchResult::class.java.classLoader)
            )
        }
    }

}

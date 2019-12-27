package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.core.util.ObjectsCompat
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.requireAbsPlayer
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.writeAbsPlayer
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
                    it.requireAbsPlayer(),
                    it.requireAbsPlayer()
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (super.equals(other)) {
            if (other is HeadToHeadMatch) {
                player == other.player && opponent == other.opponent
            } else {
                true
            }
        } else {
            false
        }
    }

    override fun hashCode(): Int = ObjectsCompat.hash(result, player, opponent)

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeAbsPlayer(player, flags)
        dest.writeAbsPlayer(opponent, flags)
    }

}

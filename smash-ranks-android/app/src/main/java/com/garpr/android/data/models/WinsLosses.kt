package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.requireAbsPlayer
import com.garpr.android.extensions.writeAbsPlayer
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WinsLosses(
        @Json(name = "player") val player: AbsPlayer,
        @Json(name = "player_wins") val playerWins: Int,
        @Json(name = "opponent") val opponent: AbsPlayer,
        @Json(name = "opponent_wins") val opponentWins: Int
) : Parcelable {

    val winLossPercentages: FloatArray
        get() {
            return when {
                playerWins == 0 && opponentWins == 0 -> {
                    floatArrayOf(0f, 0f)
                }

                playerWins == 0 -> {
                    floatArrayOf(0f, 1f)
                }

                opponentWins == 0 -> {
                    floatArrayOf(1f, 0f)
                }

                else -> {
                    val totalWins: Float = (playerWins + opponentWins).toFloat()
                    val percentPlayerWins: Float = playerWins.toFloat() / totalWins
                    floatArrayOf(percentPlayerWins, 1f - percentPlayerWins)
                }
            }
        }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeAbsPlayer(player, flags)
        dest.writeInt(playerWins)
        dest.writeAbsPlayer(opponent, flags)
        dest.writeInt(opponentWins)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel {
            WinsLosses(
                    it.requireAbsPlayer(),
                    it.readInt(),
                    it.requireAbsPlayer(),
                    it.readInt()
            )
        }
    }

}

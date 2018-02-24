package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readAbsPlayer
import com.garpr.android.extensions.writeAbsPlayer
import com.google.gson.annotations.SerializedName

data class WinsLosses(
        @SerializedName("player") val player: AbsPlayer,
        @SerializedName("player_wins") val playerWins: Int,
        @SerializedName("opponent") val opponent: AbsPlayer,
        @SerializedName("opponent_wins") val opponentWins: Int
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { WinsLosses(it.readAbsPlayer(), it.readInt(),
                it.readAbsPlayer(), it.readInt()) }
    }

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
                    val percentPlayerWins: Float = playerWins.toFloat() /
                            (playerWins.toFloat() + opponentWins.toFloat())
                    floatArrayOf(percentPlayerWins, 1f - percentPlayerWins)
                }
            }
        }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeAbsPlayer(player, flags)
        dest.writeInt(playerWins)
        dest.writeAbsPlayer(opponent, flags)
        dest.writeInt(opponentWins)
    }

}

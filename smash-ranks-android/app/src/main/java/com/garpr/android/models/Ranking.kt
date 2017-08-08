package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.*
import com.google.gson.annotations.SerializedName

data class Ranking(
        @SerializedName("player") val player: AbsPlayer,
        @SerializedName("rating") val rating: Float,
        @SerializedName("rank") val rank: Int,
        @SerializedName("previous_rank") val previousRank: Int? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { Ranking(it.readAbsPlayer(), it.readFloat(), it.readInt(),
                it.readInteger()) }
    }

    override fun equals(other: Any?): Boolean {
        return other is Ranking && player == other.player
    }

    override fun hashCode() = player.hashCode()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeAbsPlayer(player, flags)
        dest.writeFloat(rating)
        dest.writeInt(rank)
        dest.writeInteger(previousRank)
    }

}

package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readInteger
import com.garpr.android.extensions.writeInteger
import com.garpr.android.misc.ParcelableUtils
import com.google.gson.JsonDeserializer
import com.google.gson.annotations.SerializedName

data class Ranking(
        @SerializedName("player") val player: AbsPlayer,
        @SerializedName("rating") val rating: Float,
        @SerializedName("rank") val rank: Int,
        @SerializedName("previous_rank") val previousRank: Int? = null
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        return other is Ranking && player.id == other.player.id
    }

    override fun hashCode() = player.hashCode()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        ParcelableUtils.writeAbsPlayer(player, dest, flags)
        dest.writeFloat(rating)
        dest.writeInt(rank)
        dest.writeInteger(previousRank)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel { Ranking(ParcelableUtils.readAbsPlayer(it), it.readFloat(),
                it.readInt(), it.readInteger()) }

        val JSON_DESERIALIZER: JsonDeserializer<Ranking> = JsonDeserializer<Ranking> { json, typeOfT, context ->
            if (json == null || json.isJsonNull) {
                return@JsonDeserializer null
            }

            val player = context.deserialize<AbsPlayer>(json, AbsPlayer::class.java)

            val jsonObject = json.asJsonObject
            val rating = jsonObject.get("rating").asFloat
            val rank = jsonObject.get("rank").asInt

            var previousRank : Int? = null
            if (jsonObject.has("previous_rank")) {
                val previousRankJson = jsonObject.get("previous_rank")

                if (!previousRankJson.isJsonNull) {
                    previousRank = previousRankJson.asInt
                }
            }

            Ranking(player, rating, rank, previousRank)
        }
    }

}

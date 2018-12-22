package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readInteger
import com.garpr.android.extensions.requireString
import com.garpr.android.extensions.writeInteger
import com.google.gson.JsonDeserializer
import com.google.gson.annotations.SerializedName

class RankedPlayer(
        id: String,
        name: String,
        @SerializedName("rating") val rating: Float,
        @SerializedName("rank") val rank: Int,
        @SerializedName("previous_rank") val previousRank: Int? = null
) : AbsPlayer(
        id,
        name
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            RankedPlayer(
                    it.requireString(),
                    it.requireString(),
                    it.readFloat(),
                    it.readInt(),
                    it.readInteger()
            )
        }

        val JSON_DESERIALIZER = JsonDeserializer<RankedPlayer> { json, typeOfT, context ->
            if (json == null || json.isJsonNull) {
                return@JsonDeserializer null
            }

            val jsonObject = json.asJsonObject

            val previousRank = if (jsonObject.has("previous_rank")) {
                val previousRankJson = jsonObject["previous_rank"]

                if (previousRankJson == null || previousRankJson.isJsonNull) {
                    Int.MIN_VALUE
                } else {
                    previousRankJson.asInt
                }
            } else {
                null
            }

            RankedPlayer(jsonObject["id"].asString, jsonObject["name"].asString,
                    jsonObject["rating"].asFloat, jsonObject["rank"].asInt, previousRank)
        }
    }

    override val kind: Kind
        get() = Kind.RANKED

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeFloat(rating)
        dest.writeInt(rank)
        dest.writeInteger(previousRank)
    }

}

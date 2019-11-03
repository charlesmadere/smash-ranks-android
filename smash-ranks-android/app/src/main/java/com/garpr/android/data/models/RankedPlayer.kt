package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readInteger
import com.garpr.android.extensions.requireString
import com.garpr.android.extensions.writeInteger
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
class RankedPlayer(
        @Json(name = "id") id: String,
        @Json(name = "name") name: String,
        @Json(name = "rating") val rating: Float,
        @Json(name = "rank") val rank: Int,
        @Json(name = "previous_rank") val previousRank: Int? = null
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

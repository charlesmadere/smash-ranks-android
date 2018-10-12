package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readOptionalInteger
import com.garpr.android.extensions.writeInteger
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
        val CREATOR = createParcel { RankedPlayer(it.readString(), it.readString(), it.readFloat(),
                it.readInt(), it.readOptionalInteger()) }
        @JvmField
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

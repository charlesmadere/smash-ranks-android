package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.google.gson.annotations.SerializedName

class FavoritePlayer(
        id: String,
        name: String,
        @SerializedName("region") val region: LiteRegion
) : AbsPlayer(
        id,
        name
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { FavoritePlayer(it.readString(), it.readString(),
                it.readParcelable(LiteRegion::class.java.classLoader)) }
    }

    override val kind = AbsPlayer.Kind.FAVORITE

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeParcelable(region, flags)
    }

}

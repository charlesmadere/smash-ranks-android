package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.misc.ParcelableUtils
import com.google.gson.annotations.SerializedName

data class PlayersBundle(
        @SerializedName("players") val players: List<AbsPlayer>? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { PlayersBundle(ParcelableUtils.readAbsPlayerList(it)) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        ParcelableUtils.writeAbsPlayerList(players, dest, flags)
    }

}

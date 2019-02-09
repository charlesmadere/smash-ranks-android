package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readAbsPlayerList
import com.garpr.android.extensions.writeAbsPlayerList
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlayersBundle(
        @Json(name = "players") val players: List<AbsPlayer>? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            PlayersBundle(it.readAbsPlayerList())
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeAbsPlayerList(players, flags)
    }

}

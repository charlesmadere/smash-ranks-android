package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readSmashCharacterMap
import com.garpr.android.extensions.writeSmashCharacterMap
import com.google.gson.annotations.SerializedName

data class SmashRoster(
        @SerializedName("players") val players: Map<String, SmashCharacter>? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { SmashRoster(it.readSmashCharacterMap()) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeSmashCharacterMap(players)
    }

}

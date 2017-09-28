package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readSmashCharacterMap
import com.garpr.android.extensions.writeSmashCharacterMap
import com.google.gson.annotations.SerializedName

data class SmashRoster(
        @SerializedName("endpoint") val endpoint: Endpoint,
        @SerializedName("roster") val roster: Map<String, SmashCharacter>? = null,
        @SerializedName("regionId") val regionId: String,
        @SerializedName("version") val version: Int
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { SmashRoster(it.readParcelable(Endpoint::class.java.classLoader),
                it.readSmashCharacterMap(), it.readString(), it.readInt()) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(endpoint, flags)
        dest.writeSmashCharacterMap(roster)
        dest.writeString(regionId)
        dest.writeInt(version)
    }

}

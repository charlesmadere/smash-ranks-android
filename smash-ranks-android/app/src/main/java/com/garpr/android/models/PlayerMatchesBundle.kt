package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.requireParcelable

import com.google.gson.annotations.SerializedName

data class PlayerMatchesBundle(
        @SerializedName("full_player") val fullPlayer: FullPlayer,
        @SerializedName("matches_bundle") val matchesBundle: MatchesBundle? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            PlayerMatchesBundle(
                it.requireParcelable(FullPlayer::class.java.classLoader),
                it.readParcelable(MatchesBundle::class.java.classLoader)
            )
        }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(fullPlayer, flags)
        dest.writeParcelable(matchesBundle, flags)
    }

}

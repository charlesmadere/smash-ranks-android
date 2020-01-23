package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.requireParcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlayerMatchesBundle(
        @Json(name = "full_player") val fullPlayer: FullPlayer,
        @Json(name = "matches_bundle") val matchesBundle: MatchesBundle? = null
) : Parcelable {

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(fullPlayer, flags)
        dest.writeParcelable(matchesBundle, flags)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel {
            PlayerMatchesBundle(
                    it.requireParcelable(FullPlayer::class.java.classLoader),
                    it.readParcelable(MatchesBundle::class.java.classLoader)
            )
        }
    }

}

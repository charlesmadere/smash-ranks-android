package com.garpr.android.data.models

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.squareup.moshi.Json

enum class BracketSource(
        private val host: String
) : Parcelable {

    @Json(name = "challonge")
    CHALLONGE("challonge.com"),

    @Json(name = "smash_gg")
    SMASH_GG("smash.gg");


    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }

        fun fromUrl(url: String?): BracketSource? {
            if (url.isNullOrBlank()) {
                return null
            }

            val host = Uri.parse(url)?.host

            if (host.isNullOrBlank()) {
                return null
            }

            return values().firstOrNull {
                host.endsWith(it.host, true)
            }
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

}

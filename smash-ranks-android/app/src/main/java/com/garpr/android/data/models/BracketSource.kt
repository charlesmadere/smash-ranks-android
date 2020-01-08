package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.toJavaUri
import com.squareup.moshi.Json

enum class BracketSource(
        private val host: String
) : Parcelable {

    @Json(name = "challonge")
    CHALLONGE("challonge.com"),

    @Json(name = "smash_gg")
    SMASH_GG("smash.gg");

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }

        fun fromUrl(url: String?): BracketSource? {
            val host = url.toJavaUri()?.host

            return if (host.isNullOrBlank()) {
                null
            } else {
                values().firstOrNull { value ->
                    host.endsWith(value.host, ignoreCase = true)
                }
            }
        }
    }

}

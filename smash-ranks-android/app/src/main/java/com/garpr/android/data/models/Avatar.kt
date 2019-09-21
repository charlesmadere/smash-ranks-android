package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Avatar(
        @Json(name = "large") val large: String? = null,
        @Json(name = "medium") val medium: String? = null,
        @Json(name = "original") val original: String? = null,
        @Json(name = "small") val small: String? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            Avatar(
                    it.readString(),
                    it.readString(),
                    it.readString(),
                    it.readString()
            )
        }
    }

    val largeButFallbackToMediumThenOriginalThenSmall: String?
        get() = if (!large.isNullOrBlank()) {
                    large
                } else if (!medium.isNullOrBlank()) {
                    medium
                } else if (!original.isNullOrBlank()) {
                    original
                } else if (!small.isNullOrBlank()) {
                   small
                } else {
                    null
                }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(large)
        dest.writeString(medium)
        dest.writeString(original)
        dest.writeString(small)
    }

}

package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.google.gson.annotations.SerializedName

data class Avatar(
        @SerializedName(LARGE) val large: String? = null,
        @SerializedName(MEDIUM) val medium: String? = null,
        @SerializedName(ORIGINAL) val original: String? = null,
        @SerializedName(SMALL) val small: String? = null
) : Parcelable {

    companion object {
        private const val LARGE = "large"
        private const val MEDIUM = "medium"
        private const val ORIGINAL = "original"
        private const val SMALL = "small"

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
        get() = if (large?.isNotBlank() == true) {
                    large
                } else if (medium?.isNotBlank() == true) {
                    medium
                } else if (original?.isNotBlank() == true) {
                    original
                } else if (small?.isNotBlank() == true) {
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

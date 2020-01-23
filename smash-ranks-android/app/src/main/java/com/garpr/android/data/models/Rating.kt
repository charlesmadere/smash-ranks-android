package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Rating(
        @Json(name = "mu") val mu: Float,
        @Json(name = "sigma") val sigma: Float
) : Parcelable {

    val rating: Float
        get() = mu - (3f * sigma)

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeFloat(mu)
        dest.writeFloat(sigma)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel {
            Rating(it.readFloat(), it.readFloat())
        }
    }

}

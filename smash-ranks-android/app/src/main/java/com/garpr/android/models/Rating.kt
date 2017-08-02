package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel

import com.google.gson.annotations.SerializedName

data class Rating(
        @SerializedName("mu") val mu: Float,
        @SerializedName("sigma") val sigma: Float,
        @SerializedName("rating") val rating: Float = mu - (3f * sigma)
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { Rating(it.readFloat(), it.readFloat(), it.readFloat()) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeFloat(mu)
        dest.writeFloat(sigma)
        dest.writeFloat(rating)
    }

}

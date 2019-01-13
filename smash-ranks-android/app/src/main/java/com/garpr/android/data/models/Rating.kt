package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.google.gson.annotations.SerializedName

data class Rating(
        @SerializedName("mu") val mu: Float,
        @SerializedName("sigma") val sigma: Float
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            Rating(it.readFloat(), it.readFloat())
        }
    }

    val rating: Float
        get() = mu - (3f * sigma)

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeFloat(mu)
        dest.writeFloat(sigma)
    }

}

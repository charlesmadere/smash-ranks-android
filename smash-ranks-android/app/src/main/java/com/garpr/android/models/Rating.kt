package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel

import com.google.gson.annotations.SerializedName

class Rating : Parcelable {

    @SerializedName("mu")
    val mu: Float

    @SerializedName("rating")
    val rating: Float

    @SerializedName("sigma")
    val sigma: Float

    @SerializedName("region")
    val region: String


    companion object {
        @JvmField
        val CREATOR = createParcel { Rating(it) }
    }

    constructor(region: String, mu: Float, sigma: Float) {
        this.region = region
        this.mu = mu
        this.sigma = sigma
        rating = this.mu - 3f * this.sigma
    }

    private constructor(source: Parcel) {
        mu = source.readFloat()
        rating = source.readFloat()
        sigma = source.readFloat()
        region = source.readString()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeFloat(mu)
        dest.writeFloat(rating)
        dest.writeFloat(sigma)
        dest.writeString(region)
    }

}

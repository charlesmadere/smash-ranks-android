package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readInteger
import com.garpr.android.extensions.requireBoolean
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.writeBoolean
import com.garpr.android.extensions.writeInteger
import com.google.gson.annotations.SerializedName

data class SmashRosterSyncResult(
        @SerializedName("success") val success: Boolean,
        @SerializedName("httpCode") val httpCode: Int? = null,
        @SerializedName("date") val date: SimpleDate = SimpleDate(),
        @SerializedName("message") val message: String? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            SmashRosterSyncResult(
                    it.requireBoolean(),
                    it.readInteger(),
                    it.requireParcelable(SimpleDate::class.java.classLoader),
                    it.readString()
            )
        }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeBoolean(success)
        dest.writeInteger(httpCode)
        dest.writeParcelable(date, flags)
        dest.writeString(message)
    }

}

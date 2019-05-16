package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readInteger
import com.garpr.android.extensions.requireBoolean
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.writeBoolean
import com.garpr.android.extensions.writeInteger
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SmashRosterSyncResult(
        @Json(name = "success") val success: Boolean,
        @Json(name = "httpCode") val httpCode: Int? = null,
        @Json(name = "date") val date: SimpleDate = SimpleDate(),
        @Json(name = "message") val message: String? = null
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

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeBoolean(success)
        dest.writeInteger(httpCode)
        dest.writeParcelable(date, flags)
        dest.writeString(message)
    }

}

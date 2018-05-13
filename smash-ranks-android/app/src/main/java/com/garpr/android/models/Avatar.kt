package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import com.google.gson.annotations.SerializedName

data class Avatar(
        @SerializedName("large") val large: String? = null,
        @SerializedName("medium") val medium: String? = null,
        @SerializedName("other") val other: String? = null,
        @SerializedName("small") val small: String? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { Avatar(it.readString(), it.readString(), it.readString(),
                it.readString()) }

        val JSON_DESERIALIZER = JsonDeserializer<Avatar> { json, typeOfT, context ->
            if (json == null || json.isJsonNull) {
                return@JsonDeserializer null
            }

            if (json.isJsonPrimitive) {
                return@JsonDeserializer Avatar(other = json.asString)
            }

            val jsonObject = json.asJsonObject
            Avatar(large = jsonObject.get("large").asString,
                    medium = jsonObject.get("medium").asString,
                    small = jsonObject.get("small").asString)
        }
    }

    val mediumButFallbackToLargeThenSmallThenOther: String?
        get() = if (medium?.isNotBlank() == true) {
                    medium
                } else if (large?.isNotBlank() == true) {
                    large
                } else if (small?.isNotBlank() == true) {
                   small
                } else if (other?.isNotBlank() == true) {
                    other
                } else {
                    null
                }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(large)
        dest.writeString(medium)
        dest.writeString(other)
        dest.writeString(small)
    }

}

package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.google.gson.JsonDeserializer
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
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
        val CREATOR = createParcel { Avatar(it.readString(), it.readString(), it.readString(),
                it.readString()) }

        val JSON_DESERIALIZER = JsonDeserializer<Avatar> { json, typeOfT, context ->
            if (json == null || json.isJsonNull) {
                return@JsonDeserializer null
            }

            if (json.isJsonPrimitive) {
                return@JsonDeserializer Avatar(original = json.asString)
            }

            val jsonObject = json.asJsonObject

            val large = if (jsonObject.has(LARGE)) {
                jsonObject.get(LARGE).asString
            } else {
                null
            }

            val medium = if (jsonObject.has(MEDIUM)) {
                jsonObject.get(MEDIUM).asString
            } else {
                null
            }

            val original = if (jsonObject.has(ORIGINAL)) {
                jsonObject.get(ORIGINAL).asString
            } else {
                null
            }

            val small = if (jsonObject.has(SMALL)) {
                jsonObject.get(SMALL).asString
            } else {
                null
            }

            Avatar(large, medium, original, small)
        }

        val JSON_SERIALIZER = JsonSerializer<Avatar> { src, typeOfSrc, context ->
            if (src == null || src.large.isNullOrBlank() && src.medium.isNullOrBlank() &&
                    src.small.isNullOrBlank() && src.original.isNullOrBlank()) {
                null
            } else if (src.large.isNullOrBlank() && src.medium.isNullOrBlank() &&
                    src.small.isNullOrBlank() && src.original?.isNotBlank() == true) {
                JsonPrimitive(src.original)
            } else {
                val jsonObject = JsonObject()

                if (!src.large.isNullOrBlank()) {
                    jsonObject.addProperty(LARGE, src.large)
                }

                if (!src.medium.isNullOrBlank()) {
                    jsonObject.addProperty(MEDIUM, src.medium)
                }

                if (!src.original.isNullOrBlank()) {
                    jsonObject.addProperty(ORIGINAL, src.original)
                }

                if (!src.small.isNullOrBlank()) {
                    jsonObject.addProperty(SMALL, src.small)
                }

                jsonObject
            }
        }
    }

    val mediumButFallbackToLargeThenOriginalThenSmall: String?
        get() = if (medium?.isNotBlank() == true) {
                    medium
                } else if (large?.isNotBlank() == true) {
                    large
                } else if (original?.isNotBlank() == true) {
                    original
                } else if (small?.isNotBlank() == true) {
                   small
                } else {
                    null
                }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(large)
        dest.writeString(medium)
        dest.writeString(original)
        dest.writeString(small)
    }

}

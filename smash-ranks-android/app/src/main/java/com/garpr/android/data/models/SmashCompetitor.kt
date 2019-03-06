package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readStringMap
import com.garpr.android.extensions.requireString
import com.garpr.android.extensions.writeStringMap
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SmashCompetitor(
        @Json(name = "avatar") val avatar: Avatar? = null,
        @Json(name = "mains") val mains: List<SmashCharacter?>? = null,
        @Json(name = "websites") val websites: Map<String, String>? = null,
        @Json(name = "id") val id: String,
        @Json(name = "name") val name: String,
        @Json(name = "tag") val tag: String
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            SmashCompetitor(
                    it.readParcelable(Avatar::class.java.classLoader),
                    it.createTypedArrayList(SmashCharacter.CREATOR),
                    it.readStringMap(),
                    it.requireString(),
                    it.requireString(),
                    it.requireString()
            )
        }
    }

    val filteredMains: Array<SmashCharacter>?
        get() {
            if (mains.isNullOrEmpty()) {
                return null
            }

            val filteredMains = mains.filterNotNull().distinct()

            return if (filteredMains.isEmpty()) {
                null
            } else {
                filteredMains.toTypedArray()
            }
        }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(avatar, flags)
        dest.writeTypedList(mains)
        dest.writeStringMap(websites)
        dest.writeString(id)
        dest.writeString(name)
        dest.writeString(tag)
    }

}
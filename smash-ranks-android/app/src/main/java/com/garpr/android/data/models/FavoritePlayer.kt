package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.requireString
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class FavoritePlayer(
        @Json(name = "id") id: String,
        @Json(name = "name") name: String,
        @Json(name = "region") val region: Region
) : AbsPlayer(
        id,
        name
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            FavoritePlayer(
                    it.requireString(),
                    it.requireString(),
                    it.requireParcelable(Region::class.java.classLoader)
            )
        }
    }

    override val kind: Kind
        get() = Kind.FAVORITE

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeParcelable(region, flags)
    }

}

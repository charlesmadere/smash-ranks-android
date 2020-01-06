package com.garpr.android.data.models

import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.requireString
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class LitePlayer(
        @Json(name = "id") id: String,
        @Json(name = "name") name: String
) : AbsPlayer(
        id,
        name
), Parcelable {

    override val kind: Kind
        get() = Kind.LITE

    companion object {
        @JvmField
        val CREATOR = createParcel {
            LitePlayer(
                    it.requireString(),
                    it.requireString()
            )
        }
    }

}

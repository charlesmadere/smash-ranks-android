package com.garpr.android.data.models

import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.requireString
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class LiteTournament(
        @Json(name = "regions") regions: List<String>? = null,
        @Json(name = "date") date: SimpleDate,
        @Json(name = "id") id: String,
        @Json(name = "name") name: String
) : AbsTournament(
        regions,
        date,
        id,
        name
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            LiteTournament(
                    it.createStringArrayList(),
                    it.requireParcelable(SimpleDate::class.java.classLoader),
                    it.requireString(),
                    it.requireString()
            )
        }
    }

    override val kind: Kind
        get() = Kind.LITE

}

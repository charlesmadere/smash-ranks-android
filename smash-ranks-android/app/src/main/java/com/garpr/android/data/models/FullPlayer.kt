package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.optRatingsMap
import com.garpr.android.extensions.requireString
import com.garpr.android.extensions.writeRatingsMap
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class FullPlayer(
        @Json(name = "id") id: String,
        @Json(name = "name") name: String,
        @Json(name = "aliases") val aliases: List<String>? = null,
        @Json(name = "regions") val regions: List<String>? = null,
        @Json(name = "ratings") val ratings: Map<String, Rating>? = null
) : AbsPlayer(
        id,
        name
), Parcelable {

    val uniqueAliases: Array<String>? by lazy {
        if (aliases.isNullOrEmpty()) {
            return@lazy null
        }

        val uniqueAliases = mutableListOf<String>()

        aliases
                .filter { alias ->
                    alias.isNotBlank() && !alias.equals(name, ignoreCase = true)
                }
                .forEach { alias ->
                    val alreadyExists = uniqueAliases.any { uniqueAlias ->
                        alias.equals(uniqueAlias, ignoreCase = true)
                    }

                    if (!alreadyExists) {
                        uniqueAliases.add(alias)
                    }
                }

        if (uniqueAliases.isEmpty()) {
            null
        } else {
            uniqueAliases.toTypedArray()
        }
    }

    override val kind: Kind
        get() = Kind.FULL

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeStringList(aliases)
        dest.writeStringList(regions)
        dest.writeRatingsMap(ratings)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel {
            FullPlayer(
                    it.requireString(),
                    it.requireString(),
                    it.createStringArrayList(),
                    it.createStringArrayList(),
                    it.optRatingsMap()
            )
        }
    }

}

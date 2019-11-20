package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readRatingsMap
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

    companion object {
        @JvmField
        val CREATOR = createParcel {
            FullPlayer(
                    it.requireString(),
                    it.requireString(),
                    it.createStringArrayList(),
                    it.createStringArrayList(),
                    it.readRatingsMap()
            )
        }
    }

    override val kind: Kind
        get() = Kind.FULL

    val uniqueAliases: Array<String>?
        get() {
            if (aliases.isNullOrEmpty()) {
                return null
            }

            val uniqueAliases = mutableListOf<String>()

            aliases.filter { it.isNotBlank() && !it.equals(name, true) }
                    .forEach { alias ->
                        var add = true

                        for (uniqueAlias in uniqueAliases) {
                            if (alias.equals(uniqueAlias, true)) {
                                add = false
                                break
                            }
                        }

                        if (add) {
                            uniqueAliases.add(alias)
                        }
                    }

            return if (uniqueAliases.isEmpty()) {
                null
            } else {
                uniqueAliases.toTypedArray()
            }
        }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeStringList(aliases)
        dest.writeStringList(regions)
        dest.writeRatingsMap(ratings)
    }

}

package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readRatingsMap
import com.garpr.android.extensions.requireString
import com.garpr.android.extensions.writeRatingsMap
import com.google.gson.annotations.SerializedName

class FullPlayer(
        id: String,
        name: String,
        @SerializedName("aliases") val aliases: List<String>? = null,
        @SerializedName("regions") val regions: List<String>? = null,
        @SerializedName("ratings") val ratings: Map<String, Rating>? = null
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

    override val kind
        get() = Kind.FULL

    val uniqueAliases: Array<String>?
        get() {
            if (aliases == null || aliases.isEmpty()) {
                return null
            }

            val uniqueAliases = mutableListOf<String>()

            for (alias in aliases) {
                if (alias.isNotBlank() && !alias.equals(name, true)) {
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

package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.squareup.moshi.Json
import java.util.Comparator

abstract class AbsPlayer(
        val id: String,
        val name: String
) : Parcelable {

    abstract val kind: Kind

    final override fun equals(other: Any?): Boolean {
        return safeEquals(this, other)
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = name

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
    }

    companion object {
        val ALPHABETICAL_ORDER = Comparator<AbsPlayer> { o1, o2 ->
            var result = 0

            if (o1 is FavoritePlayer && o2 is FavoritePlayer) {
                result = AbsRegion.ENDPOINT_ORDER.compare(o1.region, o2.region)
            }

            if (result == 0) {
                result = o1.name.compareTo(o2.name, ignoreCase = true)
            }

            result
        }

        fun safeEquals(p1: Any?, p2: Any?): Boolean {
            return if (p1 is AbsPlayer && p2 is AbsPlayer) {
                safeEquals(p1, p2)
            } else {
                false
            }
        }

        fun safeEquals(p1: AbsPlayer, p2: AbsPlayer): Boolean {
            return if (p1.id.equals(p2.id, ignoreCase = true)) {
                if (p1 is FavoritePlayer && p2 is FavoritePlayer) {
                    p1.region.endpoint == p2.region.endpoint
                } else {
                    true
                }
            } else {
                false
            }
        }
    }

    enum class Kind : Parcelable {

        @Json(name = "favorite")
        FAVORITE,

        @Json(name = "full")
        FULL,

        @Json(name = "lite")
        LITE,

        @Json(name = "ranked")
        RANKED;

        override fun describeContents(): Int = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(ordinal)
        }

        companion object {
            @JvmField
            val CREATOR = createParcel { values()[it.readInt()] }
        }

    }

}

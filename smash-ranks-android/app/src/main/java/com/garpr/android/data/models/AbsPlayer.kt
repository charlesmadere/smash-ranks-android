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
    }

    override fun equals(other: Any?): Boolean {
        return other is AbsPlayer && id.equals(other.id, ignoreCase = true)
    }

    override fun hashCode(): Int = id.hashCode()

    abstract val kind: Kind

    override fun toString(): String = name

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
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

        companion object {
            @JvmField
            val CREATOR = createParcel { values()[it.readInt()] }
        }

        override fun describeContents(): Int = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(ordinal)
        }
    }

}

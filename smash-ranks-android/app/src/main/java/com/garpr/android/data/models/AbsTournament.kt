package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.squareup.moshi.Json
import java.util.Comparator

abstract class AbsTournament(
        val regions: List<String>? = null,
        val date: SimpleDate,
        val id: String,
        val name: String
) : Parcelable {

    companion object {
        val CHRONOLOGICAL_ORDER = Comparator<AbsTournament> { o1, o2 ->
            SimpleDate.CHRONOLOGICAL_ORDER.compare(o1.date, o2.date)
        }

        val REVERSE_CHRONOLOGICAL_ORDER = Comparator<AbsTournament> { o1, o2 ->
            CHRONOLOGICAL_ORDER.compare(o2, o1)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is AbsTournament && id.equals(other.id, ignoreCase = true)
    }

    override fun hashCode(): Int = id.hashCode()

    abstract val kind: Kind

    override fun toString(): String = name

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeStringList(regions)
        dest.writeParcelable(date, flags)
        dest.writeString(id)
        dest.writeString(name)
    }


    enum class Kind : Parcelable {
        @Json(name = "full")
        FULL,

        @Json(name = "lite")
        LITE;

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

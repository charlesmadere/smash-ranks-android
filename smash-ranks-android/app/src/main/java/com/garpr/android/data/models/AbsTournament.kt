package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.google.gson.JsonDeserializer
import com.google.gson.annotations.SerializedName
import java.util.Comparator

abstract class AbsTournament(
        @SerializedName("regions") val regions: List<String>? = null,
        @SerializedName("date") val date: SimpleDate,
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String
) : Parcelable {

    companion object {
        val CHRONOLOGICAL_ORDER = Comparator<AbsTournament> { o1, o2 ->
            SimpleDate.CHRONOLOGICAL_ORDER.compare(o1.date, o2.date)
        }

        val REVERSE_CHRONOLOGICAL_ORDER = Comparator<AbsTournament> { o1, o2 ->
            CHRONOLOGICAL_ORDER.compare(o2, o1)
        }

        val JSON_DESERIALIZER = JsonDeserializer<AbsTournament> { json, typeOfT, context ->
            if (json == null || json.isJsonNull) {
                return@JsonDeserializer null
            }

            val jsonObject = json.asJsonObject

            if (jsonObject.has("matches") ||
                    jsonObject.has("players") ||
                    jsonObject.has("raw_id")) {
                context.deserialize<AbsTournament>(json, FullTournament::class.java)
            } else {
                context.deserialize<AbsTournament>(json, LiteTournament::class.java)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is AbsTournament && id.equals(other.id, ignoreCase = true)
    }

    abstract val kind: Kind

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = name

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeStringList(regions)
        dest.writeParcelable(date, flags)
        dest.writeString(id)
        dest.writeString(name)
    }


    enum class Kind : Parcelable {
        @SerializedName("full")
        FULL,

        @SerializedName("lite")
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

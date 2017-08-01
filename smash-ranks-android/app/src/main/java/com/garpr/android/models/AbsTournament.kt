package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.google.gson.JsonDeserializer
import com.google.gson.annotations.SerializedName
import java.util.*

abstract class AbsTournament : Parcelable {

    @SerializedName("regions")
    var regions: ArrayList<String>? = null
        protected set

    @SerializedName("date")
    lateinit var date: SimpleDate
        protected set

    @SerializedName("id")
    lateinit var id: String
        protected set

    @SerializedName("name")
    lateinit var name: String
        protected set


    companion object {
        val CHRONOLOGICAL_ORDER: Comparator<AbsTournament> = Comparator { o1, o2 ->
            SimpleDate.CHRONOLOGICAL_ORDER.compare(o1.date, o2.date)
        }

        val REVERSE_CHRONOLOGICAL_ORDER: Comparator<AbsTournament> = Comparator { o1, o2 ->
            CHRONOLOGICAL_ORDER.compare(o2, o1)
        }

        val JSON_DESERIALIZER: JsonDeserializer<AbsTournament> = JsonDeserializer<AbsTournament> { json, typeOfT, context ->
            if (json == null || json.isJsonNull) {
                return@JsonDeserializer null
            }

            val jsonObject = json.asJsonObject

            if (jsonObject.has("matches") || jsonObject.has("players") || jsonObject.has(
                    "raw_id")) {
                context.deserialize<AbsTournament>(json, FullTournament::class.java)
            } else {
                context.deserialize<AbsTournament>(json, LiteTournament::class.java)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is AbsTournament && id == other.id
    }

    abstract val kind: Kind

    override fun hashCode() = id.hashCode()

    override fun describeContents() = 0

    protected open fun readFromParcel(source: Parcel) {
        regions = source.createStringArrayList()
        date = source.readParcelable<SimpleDate>(SimpleDate::class.java.classLoader)
        id = source.readString()
        name = source.readString()
    }

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
            val CREATOR = createParcel { values()[it.readInt()] }
        }

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(ordinal)
        }
    }

}

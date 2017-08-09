package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import com.google.gson.annotations.SerializedName
import java.util.*

abstract class AbsPlayer(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String
) : Parcelable {

    companion object {
        val ALPHABETICAL_ORDER: Comparator<AbsPlayer> = Comparator { o1, o2 ->
            o1.name.compareTo(o2.name, ignoreCase = true)
        }

        val JSON_DESERIALIZER: JsonDeserializer<AbsPlayer> = JsonDeserializer<AbsPlayer> { json, typeOfT, context ->
            if (json == null || json.isJsonNull) {
                return@JsonDeserializer null
            }

            val jsonObject = json.asJsonObject

            if (jsonObject.has("region")) {
                context.deserialize<FavoritePlayer>(json, FavoritePlayer::class.java)
            } else if (jsonObject.has("aliases") ||
                    jsonObject.has("regions") ||
                    jsonObject.has("ratings")) {
                context.deserialize<FullPlayer>(json, FullPlayer::class.java)
            } else if (jsonObject.has("ranked") ||
                    jsonObject.has("rating") ||
                    jsonObject.has("previous_rank")) {
                context.deserialize<RankedPlayer>(json, RankedPlayer::class.java)
            } else {
                context.deserialize<LitePlayer>(json, LitePlayer::class.java)
            }
        }

        val JSON_SERIALIZER: JsonSerializer<AbsPlayer> = JsonSerializer { src, typeOfSrc, context ->
            if (src == null) {
                return@JsonSerializer null
            }

            when (src.kind) {
                Kind.FAVORITE -> return@JsonSerializer context.serialize(src, FavoritePlayer::class.java)
                Kind.FULL -> return@JsonSerializer context.serialize(src, FullPlayer::class.java)
                Kind.LITE -> return@JsonSerializer context.serialize(src, LitePlayer::class.java)
                Kind.RANKED -> return@JsonSerializer context.serialize(src, RankedPlayer::class.java)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is AbsPlayer && id.equals(other.id, ignoreCase = true)
    }

    override fun hashCode() = id.hashCode()

    abstract val kind: Kind

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
    }


    enum class Kind : Parcelable {
        @SerializedName("favorite")
        FAVORITE,

        @SerializedName("full")
        FULL,

        @SerializedName("lite")
        LITE,

        @SerializedName("ranked")
        RANKED;

        companion object {
            @JvmField
            val CREATOR = createParcel { values()[it.readInt()] }
        }

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(ordinal)
        }
    }

}

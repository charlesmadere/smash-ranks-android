package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.*
import com.google.gson.JsonDeserializer
import com.google.gson.annotations.SerializedName
import java.util.*

data class Match(
        @SerializedName("opponent") val opponent: AbsPlayer,
        @SerializedName("tournament") val tournament: AbsTournament,
        @SerializedName("result") val result: Result
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { Match(it.readAbsPlayer(), it.readAbsTournament(),
                it.readParcelable(Result::class.java.classLoader)) }

        val CHRONOLOGICAL_ORDER: Comparator<Match> = Comparator { o1, o2 ->
            SimpleDate.CHRONOLOGICAL_ORDER.compare(o1.tournament.date, o2.tournament.date)
        }

        val REVERSE_CHRONOLOGICAL_ORDER: Comparator<Match> = Comparator { o1, o2 ->
            CHRONOLOGICAL_ORDER.compare(o2, o1)
        }

        val JSON_DESERIALIZER: JsonDeserializer<Match> = JsonDeserializer<Match> { json, typeOfT, context ->
            if (json == null || json.isJsonNull) {
                return@JsonDeserializer null
            }

            val jsonObject = json.asJsonObject

            val player = LitePlayer(jsonObject.get("opponent_id").asString,
                    jsonObject.get("opponent_name").asString)
            val tournament = LiteTournament(null,
                    context.deserialize(jsonObject.get("tournament_date"), SimpleDate::class.java),
                    jsonObject.get("tournament_id").asString,
                    jsonObject.get("tournament_name").asString)
            val result = context.deserialize<Result>(jsonObject.get("result"), Result::class.java)

            Match(player, tournament, result)
        }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeAbsPlayer(opponent, flags)
        dest.writeAbsTournament(tournament, flags)
        dest.writeParcelable(result, flags)
    }


    enum class Result : Parcelable {
        @SerializedName("excluded")
        EXCLUDED,

        @SerializedName("lose")
        LOSE,

        @SerializedName("win")
        WIN;

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

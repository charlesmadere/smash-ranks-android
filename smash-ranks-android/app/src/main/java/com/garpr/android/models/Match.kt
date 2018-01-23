package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.*
import com.garpr.android.misc.MiscUtils
import com.google.gson.JsonDeserializer
import com.google.gson.annotations.SerializedName
import java.util.*

class Match(
        result: MatchResult,
        @SerializedName("opponent") val opponent: AbsPlayer,
        @SerializedName("tournament") val tournament: AbsTournament
) : AbsMatch(
        result
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { Match(it.readParcelable(MatchResult::class.java.classLoader),
                it.readAbsPlayer(), it.readAbsTournament()) }

        val CHRONOLOGICAL_ORDER = Comparator<Match> { o1, o2 ->
            SimpleDate.CHRONOLOGICAL_ORDER.compare(o1.tournament.date, o2.tournament.date)
        }

        val REVERSE_CHRONOLOGICAL_ORDER = Comparator<Match> { o1, o2 ->
            CHRONOLOGICAL_ORDER.compare(o2, o1)
        }

        val JSON_DESERIALIZER = JsonDeserializer<Match> { json, typeOfT, context ->
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
            val result = context.deserialize<MatchResult>(jsonObject.get("result"), MatchResult::class.java)

            Match(result, player, tournament)
        }
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other) && other is Match && opponent == other.opponent
                && tournament == other.tournament
    }

    override fun hashCode(): Int {
        return MiscUtils.hash(result, opponent, tournament)
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeAbsPlayer(opponent, flags)
        dest.writeAbsTournament(tournament, flags)
    }

}

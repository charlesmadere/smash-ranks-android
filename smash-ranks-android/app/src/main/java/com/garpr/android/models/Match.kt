package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readAbsPlayer
import com.garpr.android.extensions.readAbsTournament
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.writeAbsPlayer
import com.garpr.android.extensions.writeAbsTournament
import com.garpr.android.misc.MiscUtils
import com.google.gson.JsonDeserializer
import com.google.gson.annotations.SerializedName
import java.util.Comparator

class Match(
        result: MatchResult,
        @SerializedName("opponent") val opponent: AbsPlayer,
        @SerializedName("tournament") val tournament: AbsTournament
) : AbsMatch(
        result
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { Match(it.requireParcelable(MatchResult::class.java.classLoader),
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

            val player = LitePlayer(jsonObject["opponent_id"].asString,
                    jsonObject["opponent_name"].asString)
            val tournament = LiteTournament(null,
                    context.deserialize(jsonObject["tournament_date"], SimpleDate::class.java),
                    jsonObject["tournament_id"].asString,
                    jsonObject["tournament_name"].asString)
            val result = context.deserialize<MatchResult>(jsonObject["result"],
                    MatchResult::class.java)

            Match(result, player, tournament)
        }
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other) && other is Match && opponent == other.opponent
                && tournament == other.tournament
    }

    override fun hashCode() = MiscUtils.hashCode(result, opponent, tournament)

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeAbsPlayer(opponent, flags)
        dest.writeAbsTournament(tournament, flags)
    }

}

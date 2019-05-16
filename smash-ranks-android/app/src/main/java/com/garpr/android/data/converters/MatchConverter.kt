package com.garpr.android.data.converters

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.data.models.Match
import com.garpr.android.data.models.MatchResult
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.extensions.readJsonValueMap
import com.garpr.android.extensions.requireFromJsonValue
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

object MatchConverter {

    private const val OPPONENT_ID = "opponent_id"
    private const val OPPONENT_NAME = "opponent_name"
    private const val RESULT = "result"
    private const val TOURNAMENT_DATE = "tournament_date"
    private const val TOURNAMENT_ID = "tournament_id"
    private const val TOURNAMENT_NAME = "tournament_name"


    @FromJson
    fun fromJson(
            reader: JsonReader,
            matchResultAdapter: JsonAdapter<MatchResult>,
            simpleDateAdapter: JsonAdapter<SimpleDate>
    ): Match? {
        val json = reader.readJsonValueMap() ?: return null

        val result = matchResultAdapter.requireFromJsonValue(json[RESULT])

        val opponent: AbsPlayer = LitePlayer(
                id = json[OPPONENT_ID] as String,
                name = json[OPPONENT_NAME] as String
        )

        val tournament: AbsTournament = LiteTournament(
                date = simpleDateAdapter.requireFromJsonValue(json[TOURNAMENT_DATE]),
                id = json[TOURNAMENT_ID] as String,
                name = json[TOURNAMENT_NAME] as String
        )

        return Match(
                result = result,
                opponent = opponent,
                tournament = tournament
        )
    }

    @ToJson
    fun toJson(
            writer: JsonWriter,
            value: Match?,
            matchResultAdapter: JsonAdapter<MatchResult>,
            simpleDateAdapter: JsonAdapter<SimpleDate>
    ) {
        if (value == null) {
            return
        }

        writer.beginObject()
        writer.name(RESULT).value(matchResultAdapter.toJsonValue(value.result) as String)
        writer.name(OPPONENT_ID).value(value.opponent.id)
        writer.name(OPPONENT_NAME).value(value.opponent.name)
        writer.name(TOURNAMENT_DATE).value(simpleDateAdapter.toJsonValue(value.tournament.date) as Long)
        writer.name(TOURNAMENT_ID).value(value.tournament.id)
        writer.name(TOURNAMENT_NAME).value(value.tournament.name)
        writer.endObject()
    }

}

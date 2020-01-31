package com.garpr.android.data.converters

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.data.models.MatchResult
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.data.models.TournamentMatch
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

object TournamentMatchConverter {

    private val OPPONENT_ID = 0 to "opponent_id"
    private val OPPONENT_NAME = 1 to "opponent_name"
    private val RESULT = 2 to "result"
    private val TOURNAMENT_DATE = 3 to "tournament_date"
    private val TOURNAMENT_ID = 4 to "tournament_id"
    private val TOURNAMENT_NAME = 5 to "tournament_name"

    private val OPTIONS = JsonReader.Options.of(OPPONENT_ID.second, OPPONENT_NAME.second,
            RESULT.second, TOURNAMENT_DATE.second, TOURNAMENT_ID.second, TOURNAMENT_NAME.second)

    @FromJson
    fun fromJson(
            reader: JsonReader,
            matchResultAdapter: JsonAdapter<MatchResult>,
            simpleDateAdapter: JsonAdapter<SimpleDate>
    ): TournamentMatch {
        reader.beginObject()

        var opponentId: String? = null
        var opponentName: String? = null
        var result: MatchResult? = null
        var tournamentDate: SimpleDate? = null
        var tournamentId: String? = null
        var tournamentName: String? = null

        while (reader.hasNext()) {
            when (reader.selectName(OPTIONS)) {
                OPPONENT_ID.first -> opponentId = reader.nextString()
                OPPONENT_NAME.first -> opponentName = reader.nextString()
                RESULT.first -> result = matchResultAdapter.fromJson(reader)
                TOURNAMENT_DATE.first -> tournamentDate = simpleDateAdapter.fromJson(reader)
                TOURNAMENT_ID.first -> tournamentId = reader.nextString()
                TOURNAMENT_NAME.first -> tournamentName = reader.nextString()
                else -> {
                    reader.skipName()
                    reader.skipValue()
                }
            }
        }

        reader.endObject()

        if (opponentId == null || opponentName == null || result == null ||
                tournamentDate == null || tournamentId == null || tournamentName == null) {
            throw JsonDataException("Invalid JSON data (opponentId:$opponentId, " +
                    "opponentName:$opponentName, result:$result, tournamentDate:$tournamentDate, " +
                    "tournamentId:$tournamentId, tournamentName:$tournamentName)")
        }

        val opponent: AbsPlayer = LitePlayer(
                id = opponentId,
                name = opponentName
        )

        val tournament: AbsTournament = LiteTournament(
                date = tournamentDate,
                id = tournamentId,
                name = tournamentName
        )

        return TournamentMatch(
                opponent = opponent,
                tournament = tournament,
                result = result
        )
    }

    @ToJson
    fun toJson(
            writer: JsonWriter,
            value: TournamentMatch?,
            matchResultAdapter: JsonAdapter<MatchResult>,
            simpleDateAdapter: JsonAdapter<SimpleDate>
    ) {
        if (value == null) {
            return
        }

        writer.beginObject()
                .name(RESULT.second).value(matchResultAdapter.toJsonValue(value.result) as String)
                .name(OPPONENT_ID.second).value(value.opponent.id)
                .name(OPPONENT_NAME.second).value(value.opponent.name)
                .name(TOURNAMENT_DATE.second).value(simpleDateAdapter.toJsonValue(value.tournament.date) as Long)
                .name(TOURNAMENT_ID.second).value(value.tournament.id)
                .name(TOURNAMENT_NAME.second).value(value.tournament.name)
                .endObject()
    }

}

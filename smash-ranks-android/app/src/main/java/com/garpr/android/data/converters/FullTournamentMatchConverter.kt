package com.garpr.android.data.converters

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.LitePlayer
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

object FullTournamentMatchConverter {

    private val EXCLUDED = 0 to "excluded"
    private val LOSER_ID = 1 to "loser_id"
    private val LOSER_NAME = 2 to "loser_name"
    private val MATCH_ID = 3 to "match_id"
    private val WINNER_ID = 4 to "winner_id"
    private val WINNER_NAME = 5 to "winner_name"

    private val OPTIONS = JsonReader.Options.of(EXCLUDED.second, LOSER_ID.second,
            LOSER_NAME.second, MATCH_ID.second, WINNER_ID.second, WINNER_NAME.second)

    @FromJson
    fun fromJson(reader: JsonReader): FullTournament.Match? {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull()
        }

        reader.beginObject()

        var excluded: Boolean? = null
        var loserId: String? = null
        var loserName: String? = null
        var matchId: Int? = null
        var winnerId: String? = null
        var winnerName: String? = null

        while (reader.hasNext()) {
            when (reader.selectName(OPTIONS)) {
                EXCLUDED.first -> excluded = reader.nextBoolean()
                LOSER_ID.first -> loserId = reader.nextString()
                LOSER_NAME.first -> loserName = reader.nextString()
                MATCH_ID.first -> matchId = reader.nextInt()
                WINNER_ID.first -> winnerId = reader.nextString()
                WINNER_NAME.first -> winnerName = reader.nextString()
                else -> {
                    reader.skipName()
                    reader.skipValue()
                }
            }
        }

        reader.endObject()

        if (excluded == null || loserId == null || loserName == null || matchId == null ||
                winnerId == null || winnerName == null) {
            throw JsonDataException("Invalid JSON data (excluded:$excluded, loserId:$loserId, " +
                    "loserName:$loserName, matchId:$matchId, winnerId:$winnerId, " +
                    "winnerName:$winnerName)")
        }

        val loser: AbsPlayer = LitePlayer(
                id = loserId,
                name = loserName
        )

        val winner: AbsPlayer = LitePlayer(
                id = winnerId,
                name = winnerName
        )

        return FullTournament.Match(
                loser = loser,
                winner = winner,
                excluded = excluded,
                matchId = matchId
        )
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: FullTournament.Match?) {
        if (value == null) {
            return
        }

        writer.beginObject()
                .name(EXCLUDED.second).value(value.excluded)
                .name(LOSER_ID.second).value(value.loser.id)
                .name(LOSER_NAME.second).value(value.loser.name)
                .name(MATCH_ID.second).value(value.matchId)
                .name(WINNER_ID.second).value(value.winner.id)
                .name(WINNER_NAME.second).value(value.winner.name)
                .endObject()
    }

}

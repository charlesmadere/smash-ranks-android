package com.garpr.android.data.converters

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.extensions.readJsonValueMap
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

object FullTournamentMatchConverter {

    private const val EXCLUDED = "excluded"
    private const val LOSER_ID = "loser_id"
    private const val LOSER_NAME = "loser_name"
    private const val MATCH_ID = "match_id"
    private const val WINNER_ID = "winner_id"
    private const val WINNER_NAME = "winner_name"

    @FromJson
    fun fromJson(
            reader: JsonReader
    ): FullTournament.Match? {
        val json = reader.readJsonValueMap() ?: return null

        val loser: AbsPlayer = LitePlayer(
                id = json[LOSER_ID] as String,
                name = json[LOSER_NAME] as String
        )

        val winner: AbsPlayer = LitePlayer(
                id = json[WINNER_ID] as String,
                name = json[WINNER_NAME] as String
        )

        return FullTournament.Match(
                loser = loser,
                winner = winner,
                excluded = json[EXCLUDED] as Boolean,
                matchId = (json[MATCH_ID] as Number).toInt()
        )
    }

    @ToJson
    fun toJson(
            writer: JsonWriter,
            value: FullTournament.Match?
    ) {
        if (value == null) {
            return
        }

        writer.beginObject()
                .name(EXCLUDED).value(value.excluded)
                .name(LOSER_ID).value(value.loser.id)
                .name(LOSER_NAME).value(value.loser.name)
                .name(MATCH_ID).value(value.matchId)
                .name(WINNER_ID).value(value.winner.id)
                .name(WINNER_NAME).value(value.winner.name)
                .endObject()
    }

}

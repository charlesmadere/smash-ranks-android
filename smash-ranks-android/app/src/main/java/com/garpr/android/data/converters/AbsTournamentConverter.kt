package com.garpr.android.data.converters

import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.LiteTournament
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

object AbsTournamentConverter {

    private val MATCHES = 0 to "matches"
    private val PLAYERS = 1 to "players"

    private val OPTIONS = JsonReader.Options.of(MATCHES.second, PLAYERS.second)

    @FromJson
    fun fromJson(
            reader: JsonReader,
            fullTournamentAdapter: JsonAdapter<FullTournament>,
            liteTournamentAdapter: JsonAdapter<LiteTournament>
    ): AbsTournament? {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull()
        }

        val innerReader = reader.peekJson()
        innerReader.beginObject()

        var hasMatches = false
        var hasPlayers = false

        while (innerReader.hasNext()) {
            when (innerReader.selectName(OPTIONS)) {
                MATCHES.first -> {
                    hasMatches = true
                    innerReader.skipValue()
                }
                PLAYERS.first -> {
                    hasPlayers = true
                    innerReader.skipValue()
                }
                else -> {
                    innerReader.skipName()
                    innerReader.skipValue()
                }
            }
        }

        innerReader.endObject()

        return if (hasMatches || hasPlayers) {
            fullTournamentAdapter.fromJson(reader)
        } else {
            liteTournamentAdapter.fromJson(reader)
        }
    }

    @ToJson
    fun toJson(
            writer: JsonWriter,
            value: AbsTournament?,
            fullTournamentAdapter: JsonAdapter<FullTournament>,
            liteTournamentAdapter: JsonAdapter<LiteTournament>
    ) {
        if (value == null) {
            return
        }

        when (value.kind) {
            AbsTournament.Kind.FULL -> fullTournamentAdapter.toJson(writer, value as FullTournament)
            AbsTournament.Kind.LITE -> liteTournamentAdapter.toJson(writer, value as LiteTournament)
        }
    }

}

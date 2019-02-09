package com.garpr.android.data.converters

import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.extensions.readJsonValueMap
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

object AbsTournamentConverter {

    private const val MATCHES = "matches"
    private const val PLAYERS = "players"
    private const val RAW_ID = "raw_id"


    @FromJson
    fun fromJson(
            reader: JsonReader,
            fullTournamentAdapter: JsonAdapter<FullTournament>,
            liteTournamentAdapter: JsonAdapter<LiteTournament>
    ): AbsTournament? {
        val json = reader.readJsonValueMap() ?: return null

        return if (MATCHES in json || PLAYERS in json || RAW_ID in json) {
            fullTournamentAdapter.fromJsonValue(json)
        } else {
            liteTournamentAdapter.fromJsonValue(json)
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

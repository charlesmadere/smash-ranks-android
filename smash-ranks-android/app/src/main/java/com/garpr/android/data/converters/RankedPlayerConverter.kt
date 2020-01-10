package com.garpr.android.data.converters

import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.extensions.readJsonValueMap
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

object RankedPlayerConverter {

    private const val ID = "id"
    private const val NAME = "name"
    private const val PREVIOUS_RANK = "previous_rank"
    private const val RANK = "rank"
    private const val RATING = "rating"

    @FromJson
    fun fromJson(
            reader: JsonReader
    ): RankedPlayer? {
        val json = reader.readJsonValueMap() ?: return null

        val previousRank: Int? = if (PREVIOUS_RANK in json) {
            val previousRankJson = json[PREVIOUS_RANK]

            if (previousRankJson is Number) {
                previousRankJson.toInt()
            } else {
                Int.MIN_VALUE
            }
        } else {
            null
        }

        return RankedPlayer(
                id = json[ID] as String,
                name = json[NAME] as String,
                rating = (json[RATING] as Number).toFloat(),
                rank = (json[RANK] as Number).toInt(),
                previousRank = previousRank
        )
    }

    @ToJson
    fun toJson(
            writer: JsonWriter,
            value: RankedPlayer?
    ) {
        if (value == null) {
            return
        }

        writer.beginObject()
                .name(ID).value(value.id)
                .name(NAME).value(value.name)
                .name(RATING).value(value.rating)
                .name(RANK).value(value.rank)

        if (value.previousRank != null && value.previousRank != Int.MIN_VALUE) {
            writer.name(PREVIOUS_RANK).value(value.previousRank)
        }

        writer.endObject()
    }

}

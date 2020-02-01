package com.garpr.android.data.converters

import com.garpr.android.data.models.RankedPlayer
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

object RankedPlayerConverter {

    private val ID = 0 to "id"
    private val NAME = 1 to "name"
    private val PREVIOUS_RANK = 2 to "previous_rank"
    private val RANK = 3 to "rank"
    private val RATING = 4 to "rating"

    private val OPTIONS = JsonReader.Options.of(ID.second, NAME.second, PREVIOUS_RANK.second,
            RANK.second, RATING.second)

    @FromJson
    fun fromJson(reader: JsonReader): RankedPlayer? {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull()
        }

        reader.beginObject()

        var id: String? = null
        var name: String? = null
        var rating: Float? = null
        var rank: Int? = null
        var previousRank: Int? = null

        while (reader.hasNext()) {
            when (reader.selectName(OPTIONS)) {
                ID.first -> id = reader.nextString()
                NAME.first -> name = reader.nextString()
                PREVIOUS_RANK.first -> {
                    previousRank = if (reader.peek() == JsonReader.Token.NUMBER) {
                        reader.nextInt()
                    } else {
                        reader.skipValue()
                        Int.MIN_VALUE
                    }
                }
                RATING.first -> rating = reader.nextDouble().toFloat()
                RANK.first -> rank = reader.nextInt()
                else -> {
                    reader.skipName()
                    reader.skipValue()
                }
            }
        }

        reader.endObject()

        if (id == null || name == null || rating == null || rank == null) {
            throw JsonDataException("Invalid JSON data (id:$id, name:$name, rating:$rating, " +
                    "rank:$rank, previousRank:$previousRank)")
        }

        return RankedPlayer(
                id = id,
                name = name,
                rating = rating,
                rank = rank,
                previousRank = previousRank
        )
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: RankedPlayer?) {
        if (value == null) {
            return
        }

        writer.beginObject()
                .name(ID.second).value(value.id)
                .name(NAME.second).value(value.name)
                .name(RATING.second).value(value.rating)
                .name(RANK.second).value(value.rank)

        if (value.previousRank != null && value.previousRank != Int.MIN_VALUE) {
            writer.name(PREVIOUS_RANK.second).value(value.previousRank)
        }

        writer.endObject()
    }

}

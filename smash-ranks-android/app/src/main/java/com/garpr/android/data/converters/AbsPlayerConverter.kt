package com.garpr.android.data.converters

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.RankedPlayer
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

object AbsPlayerConverter {

    private val ALIASES = 0 to "aliases"
    private val PREVIOUS_RANK = 1 to "previous_rank"
    private val RANK = 2 to "rank"
    private val RATING = 3 to "rating"
    private val RATINGS = 4 to "ratings"
    private val REGION = 5 to "region"
    private val REGIONS = 6 to "regions"

    private val OPTIONS = JsonReader.Options.of(ALIASES.second, PREVIOUS_RANK.second, RANK.second,
            RATING.second, RATINGS.second, REGION.second, REGIONS.second)

    @FromJson
    fun fromJson(
            reader: JsonReader,
            favoritePlayerAdapter: JsonAdapter<FavoritePlayer>,
            fullPlayerAdapter: JsonAdapter<FullPlayer>,
            litePlayerAdapter: JsonAdapter<LitePlayer>,
            rankedPlayerAdapter: JsonAdapter<RankedPlayer>
    ): AbsPlayer? {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull()
        }

        val innerReader = reader.peekJson()
        innerReader.beginObject()

        var hasAliases = false
        var hasPreviousRank = false
        var hasRank = false
        var hasRating = false
        var hasRatings = false
        var hasRegion = false
        var hasRegions = false

        while (innerReader.hasNext()) {
            when (innerReader.selectName(OPTIONS)) {
                ALIASES.first -> {
                    hasAliases = true
                    innerReader.skipValue()
                }
                PREVIOUS_RANK.first -> {
                    hasPreviousRank = true
                    innerReader.skipValue()
                }
                RANK.first -> {
                    hasRank = true
                    innerReader.skipValue()
                }
                RATING.first -> {
                    hasRating = true
                    innerReader.skipValue()
                }
                RATINGS.first -> {
                    hasRatings = true
                    innerReader.skipValue()
                }
                REGION.first -> {
                    hasRegion = true
                    innerReader.skipValue()
                }
                REGIONS.first -> {
                    hasRegions = true
                    innerReader.skipValue()
                }
                else -> {
                    innerReader.skipName()
                    innerReader.skipValue()
                }
            }
        }

        innerReader.endObject()

        return if (hasRegion) {
            favoritePlayerAdapter.fromJson(reader)
        } else if (hasAliases || hasRatings || hasRegions) {
            fullPlayerAdapter.fromJson(reader)
        } else if (hasPreviousRank || hasRank || hasRating) {
            rankedPlayerAdapter.fromJson(reader)
        } else {
            litePlayerAdapter.fromJson(reader)
        }
    }

    @ToJson
    fun toJson(
            writer: JsonWriter,
            value: AbsPlayer?,
            favoritePlayerAdapter: JsonAdapter<FavoritePlayer>,
            fullPlayerAdapter: JsonAdapter<FullPlayer>,
            litePlayerAdapter: JsonAdapter<LitePlayer>,
            rankedPlayerAdapter: JsonAdapter<RankedPlayer>
    ) {
        if (value == null) {
            return
        }

        when (value.kind) {
            AbsPlayer.Kind.FAVORITE -> favoritePlayerAdapter.toJson(writer, value as FavoritePlayer)
            AbsPlayer.Kind.FULL -> fullPlayerAdapter.toJson(writer, value as FullPlayer)
            AbsPlayer.Kind.LITE -> litePlayerAdapter.toJson(writer, value as LitePlayer)
            AbsPlayer.Kind.RANKED -> rankedPlayerAdapter.toJson(writer, value as RankedPlayer)
        }
    }

}

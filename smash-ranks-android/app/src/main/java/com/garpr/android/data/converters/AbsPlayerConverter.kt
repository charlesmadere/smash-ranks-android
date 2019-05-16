package com.garpr.android.data.converters

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.extensions.readJsonValueMap
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

object AbsPlayerConverter {

    private const val ALIASES = "aliases"
    private const val PREVIOUS_RANK = "previous_rank"
    private const val RANK = "rank"
    private const val RATING = "rating"
    private const val RATINGS = "ratings"
    private const val REGION = "region"
    private const val REGIONS = "regions"


    @FromJson
    fun fromJson(
            reader: JsonReader,
            favoritePlayerAdapter: JsonAdapter<FavoritePlayer>,
            fullPlayerAdapter: JsonAdapter<FullPlayer>,
            litePlayerAdapter: JsonAdapter<LitePlayer>,
            rankedPlayerAdapter: JsonAdapter<RankedPlayer>
    ): AbsPlayer? {
        val json = reader.readJsonValueMap() ?: return null

        return if (REGION in json) {
            favoritePlayerAdapter.fromJsonValue(json)
        } else if (ALIASES in json || RATINGS in json || REGIONS in json) {
            fullPlayerAdapter.fromJsonValue(json)
        } else if (PREVIOUS_RANK in json || RANK in json || RATING in json) {
            rankedPlayerAdapter.fromJsonValue(json)
        } else {
            litePlayerAdapter.fromJsonValue(json)
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

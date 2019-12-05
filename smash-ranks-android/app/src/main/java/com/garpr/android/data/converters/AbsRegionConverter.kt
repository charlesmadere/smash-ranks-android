package com.garpr.android.data.converters

import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.LiteRegion
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.readJsonValueMap
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

object AbsRegionConverter {

    private const val ENDPOINT = "endpoint"

    @FromJson
    fun fromJson(
            reader: JsonReader,
            liteRegionAdapter: JsonAdapter<LiteRegion>,
            regionAdapter: JsonAdapter<Region>
    ): AbsRegion? {
        val json = reader.readJsonValueMap() ?: return null

        return if (ENDPOINT in json) {
            regionAdapter.fromJsonValue(json)
        } else {
            liteRegionAdapter.fromJsonValue(json)
        }
    }

    @ToJson
    fun toJson(
            writer: JsonWriter,
            value: AbsRegion?,
            liteRegionAdapter: JsonAdapter<LiteRegion>,
            regionAdapter: JsonAdapter<Region>
    ) {
        if (value == null) {
            return
        }

        when (value.kind) {
            AbsRegion.Kind.LITE -> liteRegionAdapter.toJson(writer, value as LiteRegion)
            AbsRegion.Kind.FULL -> regionAdapter.toJson(writer, value as Region)
        }
    }

}

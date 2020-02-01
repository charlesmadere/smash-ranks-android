package com.garpr.android.data.converters

import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.LiteRegion
import com.garpr.android.data.models.Region
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

object AbsRegionConverter {

    private val ENDPOINT = 0 to "endpoint"

    private val OPTIONS = JsonReader.Options.of(ENDPOINT.second)

    @FromJson
    fun fromJson(
            reader: JsonReader,
            liteRegionAdapter: JsonAdapter<LiteRegion>,
            regionAdapter: JsonAdapter<Region>
    ): AbsRegion? {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull()
        }

        val innerReader = reader.peekJson()
        innerReader.beginObject()

        var hasEndpoint = false

        while (innerReader.hasNext()) {
            when (innerReader.selectName(OPTIONS)) {
                ENDPOINT.first -> {
                    hasEndpoint = true
                    innerReader.skipValue()
                }
                else -> {
                    innerReader.skipName()
                    innerReader.skipValue()
                }
            }
        }

        innerReader.endObject()

        return if (hasEndpoint) {
            regionAdapter.fromJson(reader)
        } else {
            liteRegionAdapter.fromJson(reader)
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

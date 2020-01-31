package com.garpr.android.data.converters

import com.garpr.android.data.models.SimpleDate
import com.garpr.android.extensions.require
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SimpleDateConverter {

    private val FORMATS = arrayOf(object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            return SimpleDateFormat("MM/dd/yy", Locale.US)
        }
    })

    @FromJson
    fun fromJson(reader: JsonReader): SimpleDate? {
        when (val token = reader.peek()) {
            JsonReader.Token.NULL -> {
                return reader.nextNull()
            }

            JsonReader.Token.NUMBER -> {
                val timeLong = reader.nextLong()
                return SimpleDate(Date(timeLong))
            }

            JsonReader.Token.STRING -> {
                val timeString = reader.nextString()

                FORMATS.forEach { threadLocal ->
                    val format = threadLocal.require()

                    try {
                        return SimpleDate(format.parse(timeString))
                    } catch (e: ParseException) {
                        // this Exception can be safely ignored
                    }
                }

                throw JsonDataException("Can't parse SimpleDate, unsupported format ($timeString)")
            }

            else -> {
                throw JsonDataException("Can't parse SimpleDate, unsupported token ($token)")
            }
        }
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: SimpleDate?) {
        if (value != null) {
            writer.value(value.date.time)
        }
    }

}

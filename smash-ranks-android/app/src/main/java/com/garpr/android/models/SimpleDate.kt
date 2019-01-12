package com.garpr.android.models

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.format.DateUtils
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.require
import com.google.gson.JsonDeserializer
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Comparator
import java.util.Date
import java.util.Locale

data class SimpleDate(
        val date: Date = Date()
) : Parcelable {

    companion object {
        private val FORMATS = arrayOf(object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat("MM/dd/yy", Locale.US)
            }
        })

        @JvmField
        val CREATOR = createParcel { SimpleDate(Date(it.readLong())) }

        val CHRONOLOGICAL_ORDER = Comparator<SimpleDate> { o1, o2 ->
            o1.date.compareTo(o2.date)
        }

        val REVERSE_CHRONOLOGICAL_ORDER = Comparator<SimpleDate> { o1, o2 ->
            CHRONOLOGICAL_ORDER.compare(o2, o1)
        }

        val JSON_DESERIALIZER = JsonDeserializer<SimpleDate> { json, typeOfT, context ->
            if (json == null || json.isJsonNull) {
                return@JsonDeserializer null
            }

            val jsonString = json.asString

            if (jsonString.isNullOrBlank()) {
                return@JsonDeserializer null
            }

            for (threadLocal in FORMATS) {
                val format = threadLocal.require()

                try {
                    return@JsonDeserializer SimpleDate(format.parse(jsonString))
                } catch (e: ParseException) {
                    // this Exception can be safely ignored
                }
            }

            try {
                return@JsonDeserializer SimpleDate(Date(jsonString.toLong()))
            } catch (e: NumberFormatException) {
                // this Exception can be safely ignored
            }

            throw JsonParseException("unable to parse date: $json")
        }

        val JSON_SERIALIZER = JsonSerializer<SimpleDate> { src, typeOfSrc, context ->
            if (src == null) {
                null
            } else {
                JsonPrimitive(src.date.time)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is SimpleDate && date == other.date
    }

    val fullForm: CharSequence by lazy { DateFormat.getDateInstance(DateFormat.FULL).format(date) }

    fun getRelativeDateTimeText(context: Context): CharSequence {
        return DateUtils.getRelativeDateTimeString(context, date.time, DateUtils.DAY_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS, 0)
    }

    fun happenedAfter(simpleDate: SimpleDate): Boolean {
        return date.time > simpleDate.date.time
    }

    override fun hashCode(): Int = date.hashCode()

    val mediumForm: CharSequence by lazy { DateFormat.getDateInstance(DateFormat.MEDIUM).format(date) }

    val shortForm: CharSequence by lazy { DateFormat.getDateInstance(DateFormat.SHORT).format(date) }

    override fun toString(): String = date.toString()

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(date.time)
    }

}

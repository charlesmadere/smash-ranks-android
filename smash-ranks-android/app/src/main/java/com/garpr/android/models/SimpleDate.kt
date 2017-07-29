package com.garpr.android.models

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.text.format.DateUtils
import com.garpr.android.extensions.createParcel
import com.google.gson.JsonDeserializer
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SimpleDate @JvmOverloads constructor(
        val date: Date = Date()
) : Parcelable {

    companion object {
        private val FORMATS = arrayOf(SimpleDateFormat("MM/dd/yy", Locale.US))

        val CREATOR = createParcel { SimpleDate(it) }

        val CHRONOLOGICAL_ORDER: Comparator<SimpleDate> = Comparator { o1, o2 ->
            o1.date.compareTo(o2.date)
        }

        val REVERSE_CHRONOLOGICAL_ORDER: Comparator<SimpleDate> = Comparator { o1, o2 ->
            CHRONOLOGICAL_ORDER.compare(o2, o1)
        }

        val JSON_DESERIALIZER: JsonDeserializer<SimpleDate> = JsonDeserializer<SimpleDate> { json, typeOfT, context ->
            if (json == null || json.isJsonNull) {
                return@JsonDeserializer null
            }

            val jsonString = json.asString

            if (TextUtils.isEmpty(jsonString)) {
                return@JsonDeserializer null
            }

            for (format in FORMATS) {
                try {
                    return@JsonDeserializer SimpleDate(format.parse(jsonString))
                } catch (e: ParseException) {
                    // this Exception can be safely ignored
                }

            }

            try {
                return@JsonDeserializer SimpleDate(java.lang.Long.parseLong(jsonString))
            } catch (e: NumberFormatException) {
                // this Exception can be safely ignored
            }

            throw JsonParseException("unable to parse date: " + json)
        }

        val JSON_SERIALIZER: JsonSerializer<SimpleDate> = JsonSerializer { src, typeOfSrc, context ->
            if (src == null) {
                null
            } else {
                JsonPrimitive(src.date.time)
            }
        }
    }

    constructor(date: Long) : this(Date(date))

    private constructor(source: Parcel) : this(source.readLong())

    override fun equals(other: Any?): Boolean {
        return other is SimpleDate && date == other.date
    }

    fun getRelativeDateTimeText(context: Context): CharSequence {
        return DateUtils.getRelativeDateTimeString(context, date.time, DateUtils.DAY_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS, 0)
    }

    fun happenedAfter(simpleDate: SimpleDate): Boolean {
        return date.time > simpleDate.date.time
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }

    val longForm: CharSequence = DateFormat.getDateInstance(DateFormat.LONG).format(date)

    val mediumForm: CharSequence = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)

    val shortForm: CharSequence = DateFormat.getDateInstance(DateFormat.SHORT).format(date)

    override fun toString(): String {
        return date.toString()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(date.time)
    }

}

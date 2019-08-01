package com.garpr.android.data.models

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.format.DateUtils
import com.garpr.android.extensions.createParcel
import java.text.DateFormat
import java.util.Comparator
import java.util.Date

data class SimpleDate(
        val date: Date = Date()
) : Parcelable {

    val fullForm: CharSequence by lazy { DateFormat.getDateInstance(DateFormat.FULL).format(date) }

    val mediumForm: CharSequence by lazy { DateFormat.getDateInstance(DateFormat.MEDIUM).format(date) }

    val shortForm: CharSequence by lazy { DateFormat.getDateInstance(DateFormat.SHORT).format(date) }

    companion object {
        @JvmField
        val CREATOR = createParcel { SimpleDate(Date(it.readLong())) }

        val CHRONOLOGICAL_ORDER = Comparator<SimpleDate> { o1, o2 ->
            o1.date.compareTo(o2.date)
        }

        val REVERSE_CHRONOLOGICAL_ORDER = Comparator<SimpleDate> { o1, o2 ->
            CHRONOLOGICAL_ORDER.compare(o2, o1)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is SimpleDate && date == other.date
    }

    fun getRelativeDateTimeText(context: Context): CharSequence {
        return DateUtils.getRelativeDateTimeString(context, date.time, DateUtils.DAY_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS, 0)
    }

    override fun hashCode(): Int = date.hashCode()

    override fun toString(): String = date.toString()

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(date.time)
    }

}

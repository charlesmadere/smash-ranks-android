package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes
import com.garpr.android.R
import com.garpr.android.extensions.createParcel
import com.squareup.moshi.Json
import java.util.concurrent.TimeUnit

enum class PollFrequency constructor(
        @StringRes val textResId: Int,
        timeInMillis: Long
) : Parcelable {

    @Json(name = "every_8_hours")
    EVERY_8_HOURS(R.string.every_8_hours, TimeUnit.HOURS.toMillis(8)),

    @Json(name = "daily")
    DAILY(R.string.daily, TimeUnit.DAYS.toMillis(1)),

    @Json(name = "every_2_days")
    EVERY_2_DAYS(R.string.every_2_days, TimeUnit.DAYS.toMillis(2)),

    @Json(name = "every_3_days")
    EVERY_3_DAYS(R.string.every_3_days, TimeUnit.DAYS.toMillis(3)),

    @Json(name = "every_5_days")
    EVERY_5_DAYS(R.string.every_5_days, TimeUnit.DAYS.toMillis(5)),

    @Json(name = "weekly")
    WEEKLY(R.string.weekly, TimeUnit.DAYS.toMillis(7)),

    @Json(name = "every_10_days")
    EVERY_10_DAYS(R.string.every_10_days, TimeUnit.DAYS.toMillis(10)),

    @Json(name = "every_2_weeks")
    EVERY_2_WEEKS(R.string.every_2_weeks, TimeUnit.DAYS.toMillis(14));

    val timeInSeconds: Long = TimeUnit.MILLISECONDS.toSeconds(timeInMillis)

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }
    }

}

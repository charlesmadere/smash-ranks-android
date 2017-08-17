package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.StringRes
import com.garpr.android.R
import com.garpr.android.extensions.createParcel
import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

enum class PollFrequency constructor(
        @param:StringRes val textResId: Int,
        timeInMillis: Long
) : Parcelable {

    @SerializedName("every_8_hours")
    EVERY_8_HOURS(R.string.every_8_hours, TimeUnit.HOURS.toMillis(8)),

    @SerializedName("daily")
    DAILY(R.string.daily, TimeUnit.DAYS.toMillis(1)),

    @SerializedName("every_2_days")
    EVERY_2_DAYS(R.string.every_2_days, TimeUnit.DAYS.toMillis(2)),

    @SerializedName("every_3_days")
    EVERY_3_DAYS(R.string.every_3_days, TimeUnit.DAYS.toMillis(3)),

    @SerializedName("weekly")
    WEEKLY(R.string.weekly, TimeUnit.DAYS.toMillis(7));


    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }
    }

    val timeInSeconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis)

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

}

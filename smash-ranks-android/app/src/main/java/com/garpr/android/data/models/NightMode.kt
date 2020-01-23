package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import com.garpr.android.R
import com.garpr.android.extensions.createParcel
import com.squareup.moshi.Json

enum class NightMode constructor(
        @StringRes val textResId: Int,
        @AppCompatDelegate.NightMode val themeValue: Int
) : Parcelable {

    @Json(name = "auto")
    AUTO(R.string.auto, AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY),

    @Json(name = "night_no")
    NIGHT_NO(R.string.day, AppCompatDelegate.MODE_NIGHT_NO),

    @Json(name = "night_yes")
    NIGHT_YES(R.string.night, AppCompatDelegate.MODE_NIGHT_YES),

    @Json(name = "system")
    SYSTEM(R.string.system, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }
    }

}

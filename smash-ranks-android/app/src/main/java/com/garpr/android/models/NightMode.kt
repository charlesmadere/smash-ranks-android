package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate

import com.garpr.android.R
import com.garpr.android.extensions.createParcel
import com.google.gson.annotations.SerializedName

enum class NightMode constructor(
        @StringRes val textResId: Int,
        @AppCompatDelegate.NightMode val themeValue: Int
) : Parcelable {

    @SerializedName("auto")
    AUTO(R.string.auto, AppCompatDelegate.MODE_NIGHT_AUTO),

    @SerializedName("night_no")
    NIGHT_NO(R.string.day, AppCompatDelegate.MODE_NIGHT_NO),

    @SerializedName("night_yes")
    NIGHT_YES(R.string.night, AppCompatDelegate.MODE_NIGHT_YES),

    @SerializedName("system")
    SYSTEM(R.string.system, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);


    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

}

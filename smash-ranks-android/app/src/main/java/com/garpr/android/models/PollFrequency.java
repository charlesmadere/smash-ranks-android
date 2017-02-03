package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;

import com.garpr.android.R;
import com.google.gson.annotations.SerializedName;

import java.util.concurrent.TimeUnit;

public enum PollFrequency implements Parcelable {

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

    @StringRes
    private final int mTextResId;

    private final long mTimeInMillis;


    PollFrequency(@StringRes final int textResId, final long timeInMillis) {
        mTextResId = textResId;
        mTimeInMillis = timeInMillis;
    }

    @StringRes
    public int getTextResId() {
        return mTextResId;
    }

    public long getTimeInMillis() {
        return mTimeInMillis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(ordinal());
    }

    public static final Creator<PollFrequency> CREATOR = new Creator<PollFrequency>() {
        @Override
        public PollFrequency createFromParcel(final Parcel source) {
            return values()[source.readInt()];
        }

        @Override
        public PollFrequency[] newArray(final int size) {
            return new PollFrequency[size];
        }
    };

}

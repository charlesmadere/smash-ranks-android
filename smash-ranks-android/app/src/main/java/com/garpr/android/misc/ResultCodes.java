package com.garpr.android.misc;

import android.os.Parcel;
import android.os.Parcelable;

public enum ResultCodes implements Parcelable {

    IDENTITY_SELECTED,
    REGION_SELECTED,
    RINGTONE_SELECTED;

    public final int mValue;


    ResultCodes() {
        mValue = ordinal() + 1000;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(ordinal());
    }

    public static final Creator<ResultCodes> CREATOR = new Creator<ResultCodes>() {
        @Override
        public ResultCodes createFromParcel(final Parcel source) {
            return values()[source.readInt()];
        }

        @Override
        public ResultCodes[] newArray(final int size) {
            return new ResultCodes[size];
        }
    };

}

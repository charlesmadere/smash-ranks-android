package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RankingsBundle implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {

    }

    public static final Creator<RankingsBundle> CREATOR = new Creator<RankingsBundle>() {
        @Override
        public RankingsBundle createFromParcel(final Parcel source) {
            final RankingsBundle rb = new RankingsBundle();

            return rb;
        }

        @Override
        public RankingsBundle[] newArray(final int size) {
            return new RankingsBundle[size];
        }
    };

}

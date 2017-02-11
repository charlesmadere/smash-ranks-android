package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WinsLosses implements Parcelable {

    @SerializedName("losses")
    public final int mLosses;

    @SerializedName("wins")
    public final int mWins;


    public WinsLosses(final int wins, final int losses) {
        mWins = wins;
        mLosses = losses;
    }

    private WinsLosses(final Parcel source) {
        mLosses = source.readInt();
        mWins = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(mLosses);
        dest.writeInt(mWins);
    }

    public static final Creator<WinsLosses> CREATOR = new Creator<WinsLosses>() {
        @Override
        public WinsLosses createFromParcel(final Parcel source) {
            return new WinsLosses(source);
        }

        @Override
        public WinsLosses[] newArray(final int size) {
            return new WinsLosses[size];
        }
    };

}

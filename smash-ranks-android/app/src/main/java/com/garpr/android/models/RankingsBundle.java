package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RankingsBundle implements Parcelable {

    @SerializedName("time")
    private SimpleDate mTime;

    @SerializedName("id")
    private String mId;


    @Override
    public boolean equals(final Object obj) {
        return obj instanceof RankingsBundle && mId.equals(((RankingsBundle) obj).mId);
    }

    public SimpleDate getTime() {
        return mTime;
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(mTime, flags);
        dest.writeString(mId);
    }

    public static final Creator<RankingsBundle> CREATOR = new Creator<RankingsBundle>() {
        @Override
        public RankingsBundle createFromParcel(final Parcel source) {
            final RankingsBundle rb = new RankingsBundle();
            rb.mTime = source.readParcelable(SimpleDate.class.getClassLoader());
            rb.mId = source.readString();
            return rb;
        }

        @Override
        public RankingsBundle[] newArray(final int size) {
            return new RankingsBundle[size];
        }
    };

}

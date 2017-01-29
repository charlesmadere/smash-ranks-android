package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RankingsBundle implements Parcelable {

    @Nullable
    @SerializedName("ranking")
    private ArrayList<Ranking> mRankings;

    @SerializedName("time")
    private SimpleDate mTime;

    @SerializedName("id")
    private String mId;

    @SerializedName("region")
    private String mRegion;


    @Override
    public boolean equals(final Object obj) {
        return obj instanceof RankingsBundle && mId.equals(((RankingsBundle) obj).getId());
    }

    public String getId() {
        return mId;
    }

    @Nullable
    public ArrayList<Ranking> getRankings() {
        return mRankings;
    }

    public SimpleDate getTime() {
        return mTime;
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }

    public boolean hasRankings() {
        return mRankings != null && !mRankings.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeTypedList(mRankings);
        dest.writeParcelable(mTime, flags);
        dest.writeString(mId);
        dest.writeString(mRegion);
    }

    public static final Creator<RankingsBundle> CREATOR = new Creator<RankingsBundle>() {
        @Override
        public RankingsBundle createFromParcel(final Parcel source) {
            final RankingsBundle rb = new RankingsBundle();
            rb.mRankings = source.createTypedArrayList(Ranking.CREATOR);
            rb.mTime = source.readParcelable(SimpleDate.class.getClassLoader());
            rb.mId = source.readString();
            rb.mRegion = source.readString();
            return rb;
        }

        @Override
        public RankingsBundle[] newArray(final int size) {
            return new RankingsBundle[size];
        }
    };

}

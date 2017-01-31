package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Region implements Parcelable {

    @SerializedName("ranking_activity_day_limit")
    private int mRankingActivityDayLimit;

    @SerializedName("ranking_num_tourneys_attended")
    private int mRankingNumTourneysAttended;

    @SerializedName("tournament_qualified_day_limit")
    private int mTournamentQualifiedDayLimit;

    @SerializedName("display_name")
    private String mDisplayName;

    @SerializedName("id")
    private String mId;


    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Region && mId.equals(((Region) obj).getId());
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public String getId() {
        return mId;
    }

    public int getRankingActivityDayLimit() {
        return mRankingActivityDayLimit;
    }

    public int getRankingNumTourneysAttended() {
        return mRankingNumTourneysAttended;
    }

    public int getTournamentQualifiedDayLimit() {
        return mTournamentQualifiedDayLimit;
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(mRankingActivityDayLimit);
        dest.writeInt(mRankingNumTourneysAttended);
        dest.writeInt(mTournamentQualifiedDayLimit);
        dest.writeString(mDisplayName);
        dest.writeString(mId);
    }

    public static final Creator<Region> CREATOR = new Creator<Region>() {
        @Override
        public Region createFromParcel(final Parcel source) {
            final Region r = new Region();
            r.mRankingActivityDayLimit = source.readInt();
            r.mRankingNumTourneysAttended = source.readInt();
            r.mTournamentQualifiedDayLimit = source.readInt();
            r.mDisplayName = source.readString();
            r.mId = source.readString();
            return r;
        }

        @Override
        public Region[] newArray(final int size) {
            return new Region[size];
        }
    };

}

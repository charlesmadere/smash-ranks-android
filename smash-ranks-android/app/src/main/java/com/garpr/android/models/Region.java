package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.misc.ParcelableUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class Region implements Parcelable {

    @SerializedName("endpoint")
    private Endpoint mEndpoint;

    @Nullable
    @SerializedName("ranking_activity_day_limit")
    private Integer mRankingActivityDayLimit;

    @Nullable
    @SerializedName("ranking_num_tourneys_attended")
    private Integer mRankingNumTourneysAttended;

    @Nullable
    @SerializedName("tournament_qualified_day_limit")
    private Integer mTournamentQualifiedDayLimit;

    @SerializedName("display_name")
    private String mDisplayName;

    @SerializedName("id")
    private String mId;


    public Region() {

    }

    public Region(@NonNull final String displayName, @NonNull final String id,
            @NonNull final Endpoint endpoint) {
        mDisplayName = displayName;
        mId = id;
        mEndpoint = endpoint;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Region && mId.equalsIgnoreCase(((Region) obj).getId());
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public Endpoint getEndpoint() {
        if (mEndpoint == null) {
            throw new IllegalStateException("mEndpoint is null");
        }

        return mEndpoint;
    }

    public String getId() {
        return mId;
    }

    @Nullable
    public Integer getRankingActivityDayLimit() {
        return mRankingActivityDayLimit;
    }

    @Nullable
    public Integer getRankingNumTourneysAttended() {
        return mRankingNumTourneysAttended;
    }

    @Nullable
    public Integer getTournamentQualifiedDayLimit() {
        return mTournamentQualifiedDayLimit;
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }

    public void setEndpoint(@NonNull final Endpoint endpoint) {
        mEndpoint = endpoint;
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
        dest.writeParcelable(mEndpoint, flags);
        ParcelableUtils.writeInteger(mRankingActivityDayLimit, dest);
        ParcelableUtils.writeInteger(mRankingNumTourneysAttended, dest);
        ParcelableUtils.writeInteger(mTournamentQualifiedDayLimit, dest);
        dest.writeString(mDisplayName);
        dest.writeString(mId);
    }

    public static final Creator<Region> CREATOR = new Creator<Region>() {
        @Override
        public Region createFromParcel(final Parcel source) {
            final Region r = new Region();
            r.mEndpoint = source.readParcelable(Endpoint.class.getClassLoader());
            r.mRankingActivityDayLimit = ParcelableUtils.readInteger(source);
            r.mRankingNumTourneysAttended = ParcelableUtils.readInteger(source);
            r.mTournamentQualifiedDayLimit = ParcelableUtils.readInteger(source);
            r.mDisplayName = source.readString();
            r.mId = source.readString();
            return r;
        }

        @Override
        public Region[] newArray(final int size) {
            return new Region[size];
        }
    };

    public static final Comparator<Region> ALPHABETICAL_ORDER = new Comparator<Region>() {
        @Override
        public int compare(final Region o1, final Region o2) {
            return o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName());
        }
    };

}

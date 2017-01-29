package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TournamentsBundle implements Parcelable {

    @Nullable
    @SerializedName("players")
    private ArrayList<Player> mPlayers;

    @Nullable
    @SerializedName("regions")
    private ArrayList<String> mRegions;

    @SerializedName("date")
    private SimpleDate mDate;

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("raw_id")
    private String mRawId;

    @Nullable
    @SerializedName("url")
    private String mUrl;


    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TournamentsBundle && mId.equals(((TournamentsBundle) obj).getId());
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Nullable
    public ArrayList<Player> getPlayers() {
        return mPlayers;
    }

    public String getRawId() {
        return mRawId;
    }

    @Nullable
    public ArrayList<String> getRegions() {
        return mRegions;
    }

    @Nullable
    public String getUrl() {
        return mUrl;
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeTypedList(mPlayers);
        dest.writeParcelable(mDate, flags);
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mRawId);
        dest.writeStringList(mRegions);
        dest.writeString(mUrl);
    }

    public static final Creator<TournamentsBundle> CREATOR = new Creator<TournamentsBundle>() {
        @Override
        public TournamentsBundle createFromParcel(final Parcel source) {
            final TournamentsBundle tb = new TournamentsBundle();
            tb.mPlayers = source.createTypedArrayList(Player.CREATOR);
            tb.mDate = source.readParcelable(SimpleDate.class.getClassLoader());
            tb.mId = source.readString();
            tb.mName = source.readString();
            tb.mRawId = source.readString();
            tb.mRegions = source.createStringArrayList();
            tb.mUrl = source.readString();
            return tb;
        }

        @Override
        public TournamentsBundle[] newArray(final int size) {
            return new TournamentsBundle[size];
        }
    };

}

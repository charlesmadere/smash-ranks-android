package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LiteTournament implements Parcelable {

    @Nullable
    @SerializedName("regions")
    private ArrayList<String> mRegions;

    @SerializedName("date")
    private SimpleDate mDate;

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;


    @Override
    public boolean equals(final Object obj) {
        return obj instanceof LiteTournament && mId.equals(((LiteTournament) obj).getId());
    }

    public SimpleDate getDate() {
        return mDate;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Nullable
    public ArrayList<String> getRegions() {
        return mRegions;
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
        dest.writeStringList(mRegions);
        dest.writeParcelable(mDate, flags);
        dest.writeString(mId);
        dest.writeString(mName);
    }

    public static final Creator<LiteTournament> CREATOR = new Creator<LiteTournament>() {
        @Override
        public LiteTournament createFromParcel(final Parcel source) {
            final LiteTournament lt = new LiteTournament();
            lt.mRegions = source.createStringArrayList();
            lt.mDate = source.readParcelable(SimpleDate.class.getClassLoader());
            lt.mId = source.readString();
            lt.mName = source.readString();
            return lt;
        }

        @Override
        public LiteTournament[] newArray(final int size) {
            return new LiteTournament[size];
        }
    };

}

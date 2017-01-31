package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Player implements Parcelable {

    @Nullable
    @SerializedName("aliases")
    private ArrayList<String> mAliases;

    @Nullable
    @SerializedName("regions")
    private ArrayList<String> mRegions;

    @Nullable
    @SerializedName("ratings")
    private Ratings mRatings;

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;


    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Player && mId.equals(((Player) obj).getId());
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Nullable
    public Ratings getRatings() {
        return mRatings;
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
        dest.writeStringList(mAliases);
        dest.writeStringList(mRegions);
        dest.writeParcelable(mRatings, flags);
        dest.writeString(mId);
        dest.writeString(mName);
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(final Parcel source) {
            final Player p = new Player();
            p.mAliases = source.createStringArrayList();
            p.mRegions = source.createStringArrayList();
            p.mRatings = source.readParcelable(Ratings.class.getClassLoader());
            p.mId = source.readString();
            p.mName = source.readString();
            return p;
        }

        @Override
        public Player[] newArray(final int size) {
            return new Player[size];
        }
    };

}

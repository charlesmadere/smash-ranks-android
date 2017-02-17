package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FullPlayer extends AbsPlayer implements Parcelable {

    @Nullable
    @SerializedName("aliases")
    private ArrayList<String> mAliases;

    @Nullable
    @SerializedName("regions")
    private ArrayList<String> mRegions;

    @Nullable
    @SerializedName("ratings")
    private Ratings mRatings;


    @Nullable
    public ArrayList<String> getAliases() {
        return mAliases;
    }

    @Override
    public Kind getKind() {
        return Kind.FULL;
    }

    @Nullable
    public Ratings getRatings() {
        return mRatings;
    }

    @Nullable
    public ArrayList<String> getRegions() {
        return mRegions;
    }

    public boolean hasAliases() {
        return mAliases != null && !mAliases.isEmpty();
    }

    public boolean hasRatings() {
        return mRatings != null && !mRatings.isEmpty();
    }

    public boolean hasRegions() {
        return mRegions != null && !mRegions.isEmpty();
    }

    @Override
    protected void readFromParcel(final Parcel source) {
        super.readFromParcel(source);
        mAliases = source.createStringArrayList();
        mRegions = source.createStringArrayList();
        mRatings = source.readParcelable(Ratings.class.getClassLoader());
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeStringList(mAliases);
        dest.writeStringList(mRegions);
        dest.writeParcelable(mRatings, flags);
    }

    public static final Creator<FullPlayer> CREATOR = new Creator<FullPlayer>() {
        @Override
        public FullPlayer createFromParcel(final Parcel source) {
            final FullPlayer p = new FullPlayer();
            p.readFromParcel(source);
            return p;
        }

        @Override
        public FullPlayer[] newArray(final int size) {
            return new FullPlayer[size];
        }
    };

}

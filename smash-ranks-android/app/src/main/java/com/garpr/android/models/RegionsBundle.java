package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RegionsBundle implements Parcelable {

    @Nullable
    @SerializedName("regions")
    private ArrayList<Region> mRegions;


    @Nullable
    public ArrayList<Region> getRegions() {
        return mRegions;
    }

    public boolean hasRegions() {
        return mRegions != null && !mRegions.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeTypedList(mRegions);
    }

    public static final Creator<RegionsBundle> CREATOR = new Creator<RegionsBundle>() {
        @Override
        public RegionsBundle createFromParcel(final Parcel source) {
            final RegionsBundle rb = new RegionsBundle();
            rb.mRegions = source.createTypedArrayList(Region.CREATOR);
            return rb;
        }

        @Override
        public RegionsBundle[] newArray(final int size) {
            return new RegionsBundle[size];
        }
    };

}

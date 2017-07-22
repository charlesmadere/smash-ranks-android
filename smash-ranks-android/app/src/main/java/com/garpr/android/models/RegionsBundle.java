package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegionsBundle implements Parcelable {

    @Nullable
    @SerializedName("regions")
    private ArrayList<Region> mRegions;


    @Nullable
    public ArrayList<Region> getRegions() {
        return mRegions;
    }

    public void merge(@Nullable final RegionsBundle regionsBundle) {
        final List<Region> regions = regionsBundle == null ? null : regionsBundle.getRegions();

        if (regions == null || regions.isEmpty()) {
            return;
        }

        synchronized (this) {
            if (mRegions == null) {
                mRegions = new ArrayList<>();
            }

            // noinspection ConstantConditions
            for (final Region region : regions) {
                if (!mRegions.contains(region)) {
                    mRegions.add(region);
                }
            }

            Collections.sort(mRegions, Region.ALPHABETICAL_ORDER);
        }
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

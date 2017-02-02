package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.garpr.android.misc.ParcelableUtils;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TournamentsBundle implements Parcelable {

    @Nullable
    @SerializedName("tournaments")
    private ArrayList<AbsTournament> mTournaments;


    @Nullable
    public ArrayList<AbsTournament> getTournaments() {
        return mTournaments;
    }

    public boolean hasTournaments() {
        return mTournaments != null && !mTournaments.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ParcelableUtils.writeAbsTournamentList(mTournaments, dest, flags);
    }

    public static final Creator<TournamentsBundle> CREATOR = new Creator<TournamentsBundle>() {
        @Override
        public TournamentsBundle createFromParcel(final Parcel source) {
            final TournamentsBundle tb = new TournamentsBundle();
            tb.mTournaments = ParcelableUtils.readAbsTournamentList(source);
            return tb;
        }

        @Override
        public TournamentsBundle[] newArray(final int size) {
            return new TournamentsBundle[size];
        }
    };

}

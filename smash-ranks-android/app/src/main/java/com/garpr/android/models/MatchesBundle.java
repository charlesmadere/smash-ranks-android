package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.garpr.android.misc.ParcelableUtils;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MatchesBundle implements Parcelable {

    @SerializedName("player")
    private AbsPlayer mPlayer;

    @Nullable
    @SerializedName("matches")
    private ArrayList<Match> mMatches;

    @SerializedName("losses")
    private int mLosses;

    @SerializedName("wins")
    private int mWins;


    public int getLosses() {
        return mLosses;
    }

    @Nullable
    public ArrayList<Match> getMatches() {
        return mMatches;
    }

    public AbsPlayer getPlayer() {
        return mPlayer;
    }

    public int getWins() {
        return mWins;
    }

    public boolean hasMatches() {
        return mMatches != null && !mMatches.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ParcelableUtils.writeAbsPlayer(mPlayer, dest, flags);
        dest.writeTypedList(mMatches);
        dest.writeInt(mLosses);
        dest.writeInt(mWins);
    }

    public static final Creator<MatchesBundle> CREATOR = new Creator<MatchesBundle>() {
        @Override
        public MatchesBundle createFromParcel(final Parcel source) {
            final MatchesBundle mb = new MatchesBundle();
            mb.mPlayer = ParcelableUtils.readAbsPlayer(source);
            mb.mMatches = source.createTypedArrayList(Match.CREATOR);
            mb.mLosses = source.readInt();
            mb.mWins = source.readInt();
            return mb;
        }

        @Override
        public MatchesBundle[] newArray(final int size) {
            return new MatchesBundle[size];
        }
    };

}

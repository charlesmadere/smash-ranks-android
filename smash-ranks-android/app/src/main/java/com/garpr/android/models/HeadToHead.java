package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.garpr.android.misc.ParcelableUtils;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HeadToHead implements Parcelable {

    @SerializedName("opponent")
    private AbsPlayer mOpponent;

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

    public AbsPlayer getOpponent() {
        return mOpponent;
    }

    public AbsPlayer getPlayer() {
        return mPlayer;
    }

    public int getWins() {
        return mWins;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ParcelableUtils.writeAbsPlayer(mOpponent, dest, flags);
        ParcelableUtils.writeAbsPlayer(mPlayer, dest, flags);
        dest.writeTypedList(mMatches);
        dest.writeInt(mLosses);
        dest.writeInt(mWins);
    }

    public static final Creator<HeadToHead> CREATOR = new Creator<HeadToHead>() {
        @Override
        public HeadToHead createFromParcel(final Parcel source) {
            final HeadToHead hth = new HeadToHead();
            hth.mOpponent = ParcelableUtils.readAbsPlayer(source);
            hth.mPlayer = ParcelableUtils.readAbsPlayer(source);
            hth.mMatches = source.createTypedArrayList(Match.CREATOR);
            hth.mLosses = source.readInt();
            hth.mWins = source.readInt();
            return hth;
        }

        @Override
        public HeadToHead[] newArray(final int size) {
            return new HeadToHead[size];
        }
    };

}

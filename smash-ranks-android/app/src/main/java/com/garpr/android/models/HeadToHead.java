package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HeadToHead implements Parcelable {

    @Nullable
    @SerializedName("matches")
    private ArrayList<Match> mMatches;

    @SerializedName("losses")
    private int mLosses;

    @SerializedName("wins")
    private int mWins;

    @SerializedName("opponent")
    private LitePlayer mOpponent;

    @SerializedName("player")
    private LitePlayer mPlayer;


    public int getLosses() {
        return mLosses;
    }

    @Nullable
    public ArrayList<Match> getMatches() {
        return mMatches;
    }

    public LitePlayer getOpponent() {
        return mOpponent;
    }

    public LitePlayer getPlayer() {
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
        dest.writeTypedList(mMatches);
        dest.writeInt(mLosses);
        dest.writeInt(mWins);
        dest.writeParcelable(mOpponent, flags);
        dest.writeParcelable(mPlayer, flags);
    }

    public static final Creator<HeadToHead> CREATOR = new Creator<HeadToHead>() {
        @Override
        public HeadToHead createFromParcel(final Parcel source) {
            final HeadToHead hth = new HeadToHead();
            hth.mMatches = source.createTypedArrayList(Match.CREATOR);
            hth.mLosses = source.readInt();
            hth.mWins = source.readInt();
            hth.mOpponent = source.readParcelable(LitePlayer.class.getClassLoader());
            hth.mPlayer = source.readParcelable(LitePlayer.class.getClassLoader());
            return hth;
        }

        @Override
        public HeadToHead[] newArray(final int size) {
            return new HeadToHead[size];
        }
    };

}

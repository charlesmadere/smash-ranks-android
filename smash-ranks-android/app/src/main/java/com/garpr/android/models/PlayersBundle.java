package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.garpr.android.misc.ParcelableUtils;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlayersBundle implements Parcelable {

    @Nullable
    @SerializedName("players")
    private ArrayList<AbsPlayer> mPlayers;


    @Nullable
    public ArrayList<AbsPlayer> getPlayers() {
        return mPlayers;
    }

    public boolean hasPlayers() {
        return mPlayers != null && !mPlayers.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ParcelableUtils.writeAbsPlayerList(mPlayers, dest, flags);
    }

    public static final Creator<PlayersBundle> CREATOR = new Creator<PlayersBundle>() {
        @Override
        public PlayersBundle createFromParcel(final Parcel source) {
            final PlayersBundle pb = new PlayersBundle();
            pb.mPlayers = ParcelableUtils.readAbsPlayerList(source);
            return pb;
        }

        @Override
        public PlayersBundle[] newArray(final int size) {
            return new PlayersBundle[size];
        }
    };

}

package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class PlayerMatchesBundle implements Parcelable {

    @SerializedName("full_player")
    private FullPlayer mFullPlayer;

    @Nullable
    @SerializedName("matches_bundle")
    private MatchesBundle mMatchesBundle;


    @NonNull
    public FullPlayer getFullPlayer() {
        if (mFullPlayer == null) {
            throw new RuntimeException("mFullPlayer is null");
        }

        return mFullPlayer;
    }

    @Nullable
    public MatchesBundle getMatchesBundle() {
        return mMatchesBundle;
    }

    public boolean hasMatchesBundle() {
        return mMatchesBundle != null && mMatchesBundle.hasMatches();
    }

    public void setFullPlayer(@NonNull final FullPlayer fullPlayer) {
        mFullPlayer = fullPlayer;
    }

    public void setMatchesBundle(@Nullable final MatchesBundle matchesBundle) {
        mMatchesBundle = matchesBundle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(mFullPlayer, flags);
        dest.writeParcelable(mMatchesBundle, flags);
    }

    public static final Creator<PlayerMatchesBundle> CREATOR = new Creator<PlayerMatchesBundle>() {
        @Override
        public PlayerMatchesBundle createFromParcel(final Parcel source) {
            final PlayerMatchesBundle pmb = new PlayerMatchesBundle();
            pmb.mFullPlayer = source.readParcelable(FullPlayer.class.getClassLoader());
            pmb.mMatchesBundle = source.readParcelable(MatchesBundle.class.getClassLoader());
            return pmb;
        }

        @Override
        public PlayerMatchesBundle[] newArray(final int size) {
            return new PlayerMatchesBundle[size];
        }
    };

}

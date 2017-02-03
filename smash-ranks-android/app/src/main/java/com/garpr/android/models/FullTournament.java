package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FullTournament extends AbsTournament implements Parcelable {

    @Nullable
    @SerializedName("matches")
    private ArrayList<Match> mMatches;

    @Nullable
    @SerializedName("players")
    private ArrayList<LitePlayer> mPlayers;

    @SerializedName("raw_id")
    private String mRawId;

    @Nullable
    @SerializedName("url")
    private String mUrl;

    @SerializedName("type")
    private Type mType;


    @Override
    public Kind getKind() {
        return Kind.FULL;
    }

    @Nullable
    public ArrayList<Match> getMatches() {
        return mMatches;
    }

    @Nullable
    public ArrayList<LitePlayer> getPlayers() {
        return mPlayers;
    }

    public String getRawId() {
        return mRawId;
    }

    public Type getType() {
        return mType;
    }

    @Nullable
    public String getUrl() {
        return mUrl;
    }

    @Override
    protected void readFromParcel(final Parcel source) {
        super.readFromParcel(source);
        mMatches = source.createTypedArrayList(Match.CREATOR);
        mPlayers = source.createTypedArrayList(LitePlayer.CREATOR);
        mRawId = source.readString();
        mUrl = source.readString();
        mType = source.readParcelable(Type.class.getClassLoader());
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(mMatches);
        dest.writeTypedList(mPlayers);
        dest.writeString(mRawId);
        dest.writeString(mUrl);
        dest.writeParcelable(mType, flags);
    }

    public static final Creator<FullTournament> CREATOR = new Creator<FullTournament>() {
        @Override
        public FullTournament createFromParcel(final Parcel source) {
            final FullTournament t = new FullTournament();
            t.readFromParcel(source);
            return t;
        }

        @Override
        public FullTournament[] newArray(final int size) {
            return new FullTournament[size];
        }
    };


    public static class Match implements Parcelable {
        @SerializedName("excluded")
        private boolean mExcluded;

        @SerializedName("loser_id")
        private String mLoserId;

        @SerializedName("loser_name")
        private String mLoserName;

        @SerializedName("match_id")
        private String mMatchId;

        @SerializedName("winner_id")
        private String mWinnerId;

        @SerializedName("winner_name")
        private String mWinnerName;

        public String getLoserId() {
            return mLoserId;
        }

        public String getLoserName() {
            return mLoserName;
        }

        public String getMatchId() {
            return mMatchId;
        }

        public String getWinnerId() {
            return mWinnerId;
        }

        public String getWinnerName() {
            return mWinnerName;
        }

        public boolean isExcluded() {
            return mExcluded;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeInt(mExcluded ? 1 : 0);
            dest.writeString(mLoserId);
            dest.writeString(mLoserName);
            dest.writeString(mMatchId);
            dest.writeString(mWinnerId);
            dest.writeString(mWinnerName);
        }

        public static final Creator<Match> CREATOR = new Creator<Match>() {
            @Override
            public Match createFromParcel(final Parcel source) {
                final Match m = new Match();
                m.mExcluded = source.readInt() != 0;
                m.mLoserId = source.readString();
                m.mLoserName = source.readString();
                m.mMatchId = source.readString();
                m.mWinnerId = source.readString();
                m.mWinnerName = source.readString();
                return m;
            }

            @Override
            public Match[] newArray(final int size) {
                return new Match[size];
            }
        };
    }


    public enum Type implements Parcelable {
        @SerializedName("challonge")
        CHALLONGE,

        @SerializedName("smashgg")
        SMASHGG;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeInt(ordinal());
        }

        public static final Creator<Type> CREATOR = new Creator<Type>() {
            @Override
            public Type createFromParcel(final Parcel source) {
                return values()[source.readInt()];
            }

            @Override
            public Type[] newArray(final int size) {
                return new Type[size];
            }
        };
    }

}
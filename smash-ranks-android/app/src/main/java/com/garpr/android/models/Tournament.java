package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Tournament implements Parcelable {

    @Nullable
    @SerializedName("matches")
    private ArrayList<Match> mMatches;

    @Nullable
    @SerializedName("players")
    private ArrayList<LitePlayer> mPlayers;

    @Nullable
    @SerializedName("regions")
    private ArrayList<String> mRegions;

    @SerializedName("type")
    private Type mType;

    @SerializedName("date")
    private SimpleDate mDate;

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("raw_id")
    private String mRawId;

    @Nullable
    @SerializedName("url")
    private String mUrl;


    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Tournament && mId.equals(((Tournament) obj).getId());
    }

    public SimpleDate getDate() {
        return mDate;
    }

    public String getId() {
        return mId;
    }

    @Nullable
    public ArrayList<Match> getMatches() {
        return mMatches;
    }

    public String getName() {
        return mName;
    }

    @Nullable
    public ArrayList<LitePlayer> getPlayers() {
        return mPlayers;
    }

    public String getRawId() {
        return mRawId;
    }

    @Nullable
    public ArrayList<String> getRegions() {
        return mRegions;
    }

    public Type getType() {
        return mType;
    }

    @Nullable
    public String getUrl() {
        return mUrl;
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeTypedList(mMatches);
        dest.writeTypedList(mPlayers);
        dest.writeStringList(mRegions);
        dest.writeParcelable(mType, flags);
        dest.writeParcelable(mDate, flags);
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mRawId);
        dest.writeString(mUrl);
    }

    public static final Creator<Tournament> CREATOR = new Creator<Tournament>() {
        @Override
        public Tournament createFromParcel(final Parcel source) {
            final Tournament t = new Tournament();
            t.mMatches = source.createTypedArrayList(Match.CREATOR);
            t.mPlayers = source.createTypedArrayList(LitePlayer.CREATOR);
            t.mRegions = source.createStringArrayList();
            t.mType = source.readParcelable(Type.class.getClassLoader());
            t.mDate = source.readParcelable(SimpleDate.class.getClassLoader());
            t.mId = source.readString();
            t.mName = source.readString();
            t.mRawId = source.readString();
            t.mUrl = source.readString();
            return t;
        }

        @Override
        public Tournament[] newArray(final int size) {
            return new Tournament[size];
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

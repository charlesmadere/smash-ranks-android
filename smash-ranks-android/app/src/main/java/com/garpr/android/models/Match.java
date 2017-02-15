package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class Match implements Parcelable {

    @SerializedName("result")
    private Result mResult;

    @SerializedName("opponent_id")
    private String mOpponentId;

    @SerializedName("opponent_name")
    private String mOpponentName;

    @SerializedName("tournament_date")
    private SimpleDate mTournamentDate;

    @SerializedName("tournament_id")
    private String mTournamentId;

    @SerializedName("tournament_name")
    private String mTournamentName;


    public String getOpponentId() {
        return mOpponentId;
    }

    public String getOpponentName() {
        return mOpponentName;
    }

    public Result getResult() {
        return mResult;
    }

    public SimpleDate getTournamentDate() {
        return mTournamentDate;
    }

    public String getTournamentId() {
        return mTournamentId;
    }

    public String getTournamentName() {
        return mTournamentName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(mResult, flags);
        dest.writeString(mOpponentId);
        dest.writeString(mOpponentName);
        dest.writeParcelable(mTournamentDate, flags);
        dest.writeString(mTournamentId);
        dest.writeString(mTournamentName);
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(final Parcel source) {
            final Match m = new Match();
            m.mResult = source.readParcelable(Result.class.getClassLoader());
            m.mOpponentId = source.readString();
            m.mOpponentName = source.readString();
            m.mTournamentDate = source.readParcelable(SimpleDate.class.getClassLoader());
            m.mTournamentId = source.readString();
            m.mTournamentName = source.readString();
            return m;
        }

        @Override
        public Match[] newArray(final int size) {
            return new Match[size];
        }
    };

    public static final Comparator<Match> CHRONOLOGICAL_ORDER = new Comparator<Match>() {
        @Override
        public int compare(final Match o1, final Match o2) {
            return SimpleDate.CHRONOLOGICAL_ORDER.compare(o1.getTournamentDate(),
                    o2.getTournamentDate());
        }
    };

    public static final Comparator<Match> REVERSE_CHRONOLOGICAL_ORDER = new Comparator<Match>() {
        @Override
        public int compare(final Match o1, final Match o2) {
            return CHRONOLOGICAL_ORDER.compare(o2, o1);
        }
    };


    public enum Result implements Parcelable {
        @SerializedName("excluded")
        EXCLUDED,

        @SerializedName("lose")
        LOSE,

        @SerializedName("win")
        WIN;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeInt(ordinal());
        }

        public static final Creator<Result> CREATOR = new Creator<Result>() {
            @Override
            public Result createFromParcel(final Parcel source) {
                return values()[source.readInt()];
            }

            @Override
            public Result[] newArray(final int size) {
                return new Result[size];
            }
        };
    }

}

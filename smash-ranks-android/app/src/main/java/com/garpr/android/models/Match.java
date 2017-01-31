package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Match implements Parcelable {

    @SerializedName("result")
    private Result mResult;

    @SerializedName("tournament_date")
    private SimpleDate mDate;

    @SerializedName("tournament_id")
    private String mId;

    @SerializedName("tournament_name")
    private String mName;

    @SerializedName("opponent_id")
    private String mOpponentId;

    @SerializedName("opponent_name")
    private String mOpponentName;


    public SimpleDate getDate() {
        return mDate;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getOpponentId() {
        return mOpponentId;
    }

    public String getOpponentName() {
        return mOpponentName;
    }

    public Result getResult() {
        return mResult;
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
        dest.writeParcelable(mResult, flags);
        dest.writeParcelable(mDate, flags);
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mOpponentId);
        dest.writeString(mOpponentName);
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(final Parcel source) {
            final Match m = new Match();
            m.mResult = source.readParcelable(Result.class.getClassLoader());
            m.mDate = source.readParcelable(SimpleDate.class.getClassLoader());
            m.mId = source.readString();
            m.mName = source.readString();
            m.mOpponentId = source.readString();
            m.mOpponentName = source.readString();
            return m;
        }

        @Override
        public Match[] newArray(final int size) {
            return new Match[size];
        }
    };


    public enum Result implements Parcelable {
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

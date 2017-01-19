package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ranking implements Parcelable {

    @SerializedName("rating")
    private float mRating;

    @SerializedName("rank")
    private int mRank;

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;


    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Ranking && mId.equals(((Ranking) obj).getId());
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getRank() {
        return mRank;
    }

    public float getRating() {
        return mRating;
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
        dest.writeFloat(mRating);
        dest.writeInt(mRank);
        dest.writeString(mId);
        dest.writeString(mName);
    }

    public static final Creator<Ranking> CREATOR = new Creator<Ranking>() {
        @Override
        public Ranking createFromParcel(final Parcel source) {
            final Ranking r = new Ranking();
            r.mRating = source.readFloat();
            r.mRank = source.readInt();
            r.mId = source.readString();
            r.mName = source.readString();
            return r;
        }

        @Override
        public Ranking[] newArray(final int size) {
            return new Ranking[size];
        }
    };

}

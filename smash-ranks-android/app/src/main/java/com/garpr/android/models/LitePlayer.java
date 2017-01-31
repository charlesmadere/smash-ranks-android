package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LitePlayer implements Parcelable {

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;


    @Override
    public boolean equals(final Object obj) {
        return obj instanceof LitePlayer && mId.equals(((LitePlayer) obj).getId());
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
        dest.writeString(mId);
        dest.writeString(mName);
    }

    public static final Creator<LitePlayer> CREATOR = new Creator<LitePlayer>() {
        @Override
        public LitePlayer createFromParcel(final Parcel source) {
            final LitePlayer lp = new LitePlayer();
            lp.mId = source.readString();
            lp.mName = source.readString();
            return lp;
        }

        @Override
        public LitePlayer[] newArray(final int size) {
            return new LitePlayer[size];
        }
    };

}

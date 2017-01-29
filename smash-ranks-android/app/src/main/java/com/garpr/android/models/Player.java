package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Player implements Parcelable {

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;


    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Player && mId.equals(((Player) obj).getId());
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(final Parcel source) {
            final Player p = new Player();
            p.mId = source.readString();
            p.mName = source.readString();
            return p;
        }

        @Override
        public Player[] newArray(final int size) {
            return new Player[size];
        }
    };

}

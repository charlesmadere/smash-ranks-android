package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TournamentsBundle implements Parcelable {

    @SerializedName("date")
    private SimpleDate mDate;

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;


    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TournamentsBundle && mId.equals(((TournamentsBundle) obj).getId());
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
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
        dest.writeParcelable(mDate, flags);
        dest.writeString(mId);
        dest.writeString(mName);
    }

    public static final Creator<TournamentsBundle> CREATOR = new Creator<TournamentsBundle>() {
        @Override
        public TournamentsBundle createFromParcel(final Parcel source) {
            final TournamentsBundle tb = new TournamentsBundle();
            tb.mDate = source.readParcelable(SimpleDate.class.getClassLoader());
            tb.mId = source.readString();
            tb.mName = source.readString();
            return tb;
        }

        @Override
        public TournamentsBundle[] newArray(final int size) {
            return new TournamentsBundle[size];
        }
    };

}

package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class Rating implements Parcelable {

    @SerializedName("mu")
    private final float mMu;

    @SerializedName("sigma")
    private final float mSigma;

    @SerializedName("region")
    private final String mRegion;


    public Rating(final String region, final float mu, final float sigma) {
        mRegion = region;
        mMu = mu;
        mSigma = sigma;
    }

    private Rating(final Parcel source) {
        mMu = source.readFloat();
        mSigma = source.readFloat();
        mRegion = source.readString();
    }

    public float getMu() {
        return mMu;
    }

    public String getMuTruncated() {
        return String.format(Locale.getDefault(), "%.3f", mMu);
    }

    public String getRegion() {
        return mRegion;
    }

    public float getSigma() {
        return mSigma;
    }

    public String getSigmaTruncated() {
        return String.format(Locale.getDefault(), "%.3f", mSigma);
    }

    @Override
    public String toString() {
        return getRegion();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeFloat(mMu);
        dest.writeFloat(mSigma);
        dest.writeString(mRegion);
    }

    public static final Creator<Rating> CREATOR = new Creator<Rating>() {
        @Override
        public Rating createFromParcel(final Parcel source) {
            return new Rating(source);
        }

        @Override
        public Rating[] newArray(final int size) {
            return new Rating[size];
        }
    };

}

package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.garpr.android.misc.MiscUtils;
import com.google.gson.annotations.SerializedName;

public class Rating implements Parcelable {

    @SerializedName("mu")
    private final float mMu;

    @SerializedName("rating")
    private final float mRating;

    @SerializedName("sigma")
    private final float mSigma;

    @SerializedName("region")
    private final String mRegion;


    public Rating(final String region, final float mu, final float sigma) {
        mRegion = region;
        mMu = mu;
        mSigma = sigma;
        mRating = mMu - (3f * mSigma);
    }

    private Rating(final Parcel source) {
        mMu = source.readFloat();
        mRating = source.readFloat();
        mSigma = source.readFloat();
        mRegion = source.readString();
    }

    public float getMu() {
        return mMu;
    }

    public String getMuTruncated() {
        return MiscUtils.truncateFloat(mMu);
    }

    public float getRating() {
        return mRating;
    }

    public String getRatingTruncated() {
        return MiscUtils.truncateFloat(mRating);
    }

    public String getRegion() {
        return mRegion;
    }

    public float getSigma() {
        return mSigma;
    }

    public String getSigmaTruncated() {
        return MiscUtils.truncateFloat(mSigma);
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
        dest.writeFloat(mRating);
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

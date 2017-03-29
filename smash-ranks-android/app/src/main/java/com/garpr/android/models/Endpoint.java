package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.garpr.android.R;
import com.garpr.android.misc.Constants;
import com.google.gson.annotations.SerializedName;

public enum Endpoint implements Parcelable {

    @SerializedName("gar_pr")
    GAR_PR(Constants.GAR_PR_API_PORT, R.string.gar_pr, Constants.GAR_PR_BASE_PATH),

    @SerializedName("not_gar_pr")
    NOT_GAR_PR(Constants.NOT_GAR_PR_API_PORT, R.string.not_gar_pr, Constants.NOT_GAR_PR_BASE_PATH);

    private final int mPort;

    @StringRes
    private final int mName;

    @NonNull
    private final String mPath;


    Endpoint(final int port, @StringRes final int name, @NonNull final String path) {
        mPort = port;
        mName = name;
        mPath = path;
    }

    @NonNull
    public String getApiPath() {
        return mPath + ":" + mPort + "/";
    }

    @StringRes
    public int getName() {
        return mName;
    }

    @NonNull
    public String getWebPath() {
        return mPath + "/";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(ordinal());
    }

    public static final Creator<Endpoint> CREATOR = new Creator<Endpoint>() {
        @Override
        public Endpoint createFromParcel(final Parcel source) {
            return values()[source.readInt()];
        }

        @Override
        public Endpoint[] newArray(final int size) {
            return new Endpoint[size];
        }
    };

}

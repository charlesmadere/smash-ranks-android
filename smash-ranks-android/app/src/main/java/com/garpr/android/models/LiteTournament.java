package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class LiteTournament extends AbsTournament implements Parcelable {

    public LiteTournament() {
        // only here for GSON purposes
    }

    public LiteTournament(@NonNull final String id, @NonNull final String name,
            @NonNull final SimpleDate date) {
        mId = id;
        mName = name;
        mDate = date;
    }

    @Override
    public Kind getKind() {
        return Kind.LITE;
    }

    public static final Creator<LiteTournament> CREATOR = new Creator<LiteTournament>() {
        @Override
        public LiteTournament createFromParcel(final Parcel source) {
            final LiteTournament lt = new LiteTournament();
            lt.readFromParcel(source);
            return lt;
        }

        @Override
        public LiteTournament[] newArray(final int size) {
            return new LiteTournament[size];
        }
    };

}

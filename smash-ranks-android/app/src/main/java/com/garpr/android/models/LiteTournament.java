package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LiteTournament extends AbsTournament implements Parcelable {

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

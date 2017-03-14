package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class LitePlayer extends AbsPlayer implements Parcelable {

    public LitePlayer() {

    }

    public LitePlayer(@NonNull final String id, @NonNull final String name) {
        mId = id;
        mName = name;
    }

    @Override
    public Kind getKind() {
        return Kind.LITE;
    }

    public static final Creator<LitePlayer> CREATOR = new Creator<LitePlayer>() {
        @Override
        public LitePlayer createFromParcel(final Parcel source) {
            final LitePlayer lp = new LitePlayer();
            lp.readFromParcel(source);
            return lp;
        }

        @Override
        public LitePlayer[] newArray(final int size) {
            return new LitePlayer[size];
        }
    };

}

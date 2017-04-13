package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class FavoritePlayer extends AbsPlayer implements Parcelable {

    @SerializedName("region")
    private Region mRegion;


    public FavoritePlayer() {

    }

    public FavoritePlayer(@NonNull final AbsPlayer player, @NonNull final Region region) {
        mId = player.getId();
        mName = player.getName();
        mRegion = region;
    }

    @Override
    public Kind getKind() {
        return Kind.FAVORITE;
    }

    public Region getRegion() {
        return mRegion;
    }

    @Override
    protected void readFromParcel(final Parcel source) {
        super.readFromParcel(source);
        mRegion = source.readParcelable(Region.class.getClassLoader());
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(mRegion, flags);
    }

    public static final Creator<FavoritePlayer> CREATOR = new Creator<FavoritePlayer>() {
        @Override
        public FavoritePlayer createFromParcel(final Parcel source) {
            final FavoritePlayer fp = new FavoritePlayer();
            fp.readFromParcel(source);
            return fp;
        }

        @Override
        public FavoritePlayer[] newArray(final int size) {
            return new FavoritePlayer[size];
        }
    };

}

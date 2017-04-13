package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.ParcelableUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

public class Ranking implements Parcelable {

    @SerializedName("player")
    private AbsPlayer mPlayer;

    @SerializedName("rating")
    private float mRating;

    @SerializedName("rank")
    private int mRank;

    @Nullable
    @SerializedName("previous_rank")
    private Integer mPreviousRank;


    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Ranking && getId().equals(((Ranking) obj).getId());
    }

    public String getId() {
        return mPlayer.getId();
    }

    public String getName() {
        return mPlayer.getName();
    }

    public AbsPlayer getPlayer() {
        return mPlayer;
    }

    @Nullable
    public Integer getPreviousRank() {
        return mPreviousRank;
    }

    public int getRank() {
        return mRank;
    }

    public float getRating() {
        return mRating;
    }

    public String getRatingTruncated() {
        return MiscUtils.truncateFloat(mRating);
    }

    @Override
    public int hashCode() {
        return mPlayer.hashCode();
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
        ParcelableUtils.writeAbsPlayer(mPlayer, dest, flags);
        dest.writeFloat(mRating);
        dest.writeInt(mRank);
        ParcelableUtils.writeInteger(mPreviousRank, dest);
    }

    public static final Creator<Ranking> CREATOR = new Creator<Ranking>() {
        @Override
        public Ranking createFromParcel(final Parcel source) {
            final Ranking r = new Ranking();
            r.mPlayer = ParcelableUtils.readAbsPlayer(source);
            r.mRating = source.readFloat();
            r.mRank = source.readInt();
            r.mPreviousRank = ParcelableUtils.readInteger(source);
            return r;
        }

        @Override
        public Ranking[] newArray(final int size) {
            return new Ranking[size];
        }
    };

    public static final JsonDeserializer<Ranking> JSON_DESERIALIZER = new JsonDeserializer<Ranking>() {
        @Override
        public Ranking deserialize(final JsonElement json, final Type typeOfT,
                final JsonDeserializationContext context) throws JsonParseException {
            if (json == null || json.isJsonNull()) {
                return null;
            }

            final Ranking ranking = new Ranking();
            ranking.mPlayer = context.deserialize(json, AbsPlayer.class);

            final JsonObject jsonObject = json.getAsJsonObject();
            ranking.mRating = jsonObject.get("rating").getAsFloat();
            ranking.mRank = jsonObject.get("rank").getAsInt();

            if (jsonObject.has("previous_rank")) {
                final JsonElement previousRank = jsonObject.get("previous_rank");

                if (!previousRank.isJsonNull()) {
                    ranking.mPreviousRank = previousRank.getAsInt();
                }
            }

            return ranking;
        }
    };

}

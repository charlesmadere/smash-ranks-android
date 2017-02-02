package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.ArrayList;

public abstract class AbsTournament implements Parcelable {

    @Nullable
    @SerializedName("regions")
    private ArrayList<String> mRegions;

    @SerializedName("date")
    private SimpleDate mDate;

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;


    @Override
    public boolean equals(final Object obj) {
        return obj instanceof AbsTournament && mId.equals(((AbsTournament) obj).getId());
    }

    public SimpleDate getDate() {
        return mDate;
    }

    public String getId() {
        return mId;
    }

    public abstract Kind getKind();

    public String getName() {
        return mName;
    }

    @Nullable
    public ArrayList<String> getRegions() {
        return mRegions;
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

    protected void readFromParcel(final Parcel source) {
        mRegions = source.createStringArrayList();
        mDate = source.readParcelable(SimpleDate.class.getClassLoader());
        mId = source.readString();
        mName = source.readString();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeStringList(mRegions);
        dest.writeParcelable(mDate, flags);
        dest.writeString(mId);
        dest.writeString(mName);
    }


    public enum Kind implements Parcelable {
        LITE, FULL;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeInt(ordinal());
        }

        public static final Creator<Kind> CREATOR = new Creator<Kind>() {
            @Override
            public Kind createFromParcel(final Parcel source) {
                return values()[source.readInt()];
            }

            @Override
            public Kind[] newArray(final int size) {
                return new Kind[size];
            }
        };
    }

    public static final JsonDeserializer<AbsTournament> JSON_DESERIALIZER = new JsonDeserializer<AbsTournament>() {
        @Override
        public AbsTournament deserialize(final JsonElement json, final Type typeOfT,
                final JsonDeserializationContext context) throws JsonParseException {
            if (json == null || json.isJsonNull()) {
                return null;
            }

            final JsonObject jsonObject = json.getAsJsonObject();

            if (jsonObject.has("raw_id")) {
                return context.deserialize(json, FullTournament.class);
            } else {
                return context.deserialize(json, LiteTournament.class);
            }
        }
    };

}

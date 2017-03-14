package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.Comparator;

public abstract class AbsPlayer implements Parcelable {

    @SerializedName("id")
    protected String mId;

    @SerializedName("name")
    protected String mName;


    @Override
    public boolean equals(final Object obj) {
        return obj instanceof AbsPlayer && mId.equals(((AbsPlayer) obj).getId());
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }

    public String getId() {
        return mId;
    }

    public abstract Kind getKind();

    public String getName() {
        return mName;
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
        mId = source.readString();
        mName = source.readString();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
    }

    public static final Comparator<AbsPlayer> ALPHABETICAL_ORDER = new Comparator<AbsPlayer>() {
        @Override
        public int compare(final AbsPlayer o1, final AbsPlayer o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    };

    public static final JsonDeserializer<AbsPlayer> JSON_DESERIALIZER = new JsonDeserializer<AbsPlayer>() {
        @Override
        public AbsPlayer deserialize(final JsonElement json, final Type typeOfT,
                final JsonDeserializationContext context) throws JsonParseException {
            if (json == null || json.isJsonNull()) {
                return null;
            }

            final JsonObject jsonObject = json.getAsJsonObject();

            if (jsonObject.has("aliases") || jsonObject.has("regions") || jsonObject.has("ratings")) {
                return context.deserialize(json, FullPlayer.class);
            } else {
                return context.deserialize(json, LitePlayer.class);
            }
        }
    };


    public enum Kind implements Parcelable {
        FULL, LITE;

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

}

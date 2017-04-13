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
import java.util.Comparator;

public abstract class AbsTournament implements Parcelable {

    @Nullable
    @SerializedName("regions")
    protected ArrayList<String> mRegions;

    @SerializedName("date")
    protected SimpleDate mDate;

    @SerializedName("id")
    protected String mId;

    @SerializedName("name")
    protected String mName;


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

    public boolean hasRegions() {
        return mRegions != null && !mRegions.isEmpty();
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

    public static final Comparator<AbsTournament> CHRONOLOGICAL_ORDER = new Comparator<AbsTournament>() {
        @Override
        public int compare(final AbsTournament o1, final AbsTournament o2) {
            return SimpleDate.CHRONOLOGICAL_ORDER.compare(o1.getDate(), o2.getDate());
        }
    };

    public static final Comparator<AbsTournament> REVERSE_CHRONOLOGICAL_ORDER = new Comparator<AbsTournament>() {
        @Override
        public int compare(final AbsTournament o1, final AbsTournament o2) {
            return CHRONOLOGICAL_ORDER.compare(o2, o1);
        }
    };

    public static final JsonDeserializer<AbsTournament> JSON_DESERIALIZER = new JsonDeserializer<AbsTournament>() {
        @Override
        public AbsTournament deserialize(final JsonElement json, final Type typeOfT,
                final JsonDeserializationContext context) throws JsonParseException {
            if (json == null || json.isJsonNull()) {
                return null;
            }

            final JsonObject jsonObject = json.getAsJsonObject();

            if (jsonObject.has("matches") || jsonObject.has("players") || jsonObject.has("raw_id")) {
                return context.deserialize(json, FullTournament.class);
            } else {
                return context.deserialize(json, LiteTournament.class);
            }
        }
    };


    public enum Kind implements Parcelable {
        @SerializedName("full")
        FULL,

        @SerializedName("lite")
        LITE;

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

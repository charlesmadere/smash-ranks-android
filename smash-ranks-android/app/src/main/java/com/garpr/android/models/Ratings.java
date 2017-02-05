package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

public class Ratings extends ArrayList<Rating> implements Parcelable {

    @Nullable
    public static Ratings fromJson(@Nullable final JsonElement json) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }

        final JsonObject jsonObject = json.getAsJsonObject();
        final Set<Entry<String, JsonElement>> entries = jsonObject.entrySet();

        if (entries == null || entries.isEmpty()) {
            return null;
        }

        final Ratings ratings = new Ratings(entries.size());

        for (final Entry<String, JsonElement> entry : entries) {
            final String region = entry.getKey();
            final JsonElement value = entry.getValue();

            if (value != null && !value.isJsonNull()) {
                final JsonObject valueObject = value.getAsJsonObject();
                final float mu = valueObject.get("mu").getAsFloat();
                final float sigma = valueObject.get("sigma").getAsFloat();
                ratings.add(new Rating(region, mu, sigma));
            }
        }

        if (ratings.isEmpty()) {
            return null;
        } else {
            ratings.trimToSize();
            return ratings;
        }
    }

    private Ratings(final int initialCapacity) {
        super(initialCapacity);
    }

    private Ratings(final Parcel source) {
        final int size = source.readInt();

        if (size == 0) {
            return;
        }

        ensureCapacity(size);

        for (int i = 0; i < size; ++i) {
            add((Rating) source.readParcelable(Rating.class.getClassLoader()));
        }

        trimToSize();
    }

    @Nullable
    public Rating getRegion(@NonNull final String region) {
        for (final Rating rating : this) {
            if (region.equals(rating.getRegion())) {
                return rating;
            }
        }

        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        final int size = size();
        dest.writeInt(size);

        if (size == 0) {
            return;
        }

        for (int i = 0; i < size; ++i) {
            dest.writeParcelable(get(i), flags);
        }
    }

    public static final Creator<Ratings> CREATOR = new Creator<Ratings>() {
        @Override
        public Ratings createFromParcel(final Parcel source) {
            return new Ratings(source);
        }

        @Override
        public Ratings[] newArray(final int size) {
            return new Ratings[size];
        }
    };

    public static final JsonDeserializer<Ratings> JSON_DESERIALIZER = new JsonDeserializer<Ratings>() {
        @Override
        public Ratings deserialize(final JsonElement json, final Type typeOfT,
                final JsonDeserializationContext context) throws JsonParseException {
            return fromJson(json);
        }
    };

}

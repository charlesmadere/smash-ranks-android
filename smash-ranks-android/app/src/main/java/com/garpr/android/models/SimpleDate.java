package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SimpleDate implements Parcelable {

    private static final SimpleDateFormat[] FORMATS = {
            new SimpleDateFormat("MM/dd/yy", Locale.US)
    };

    private final Date mDate;


    @Nullable
    public static SimpleDate fromJson(@Nullable final JsonElement json) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }

        final String jsonString = json.getAsString();

        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        for (final SimpleDateFormat format : FORMATS) {
            try {
                return new SimpleDate(format.parse(jsonString));
            } catch (final ParseException e) {
                // this Exception can be safely ignored
            }
        }

        throw new JsonParseException("unable to parse date: " + jsonString);
    }

    private SimpleDate(final Date date) {
        mDate = date;
    }

    private SimpleDate(final Parcel source) {
        mDate = new Date(source.readLong());
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof SimpleDate && mDate.equals(((SimpleDate) obj).getDate());
    }

    public Date getDate() {
        return mDate;
    }

    @Override
    public int hashCode() {
        return mDate.hashCode();
    }

    @Override
    public String toString() {
        return mDate.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(mDate.getTime());
    }

    public static final Creator<SimpleDate> CREATOR = new Creator<SimpleDate>() {
        @Override
        public SimpleDate createFromParcel(final Parcel source) {
            return new SimpleDate(source);
        }

        @Override
        public SimpleDate[] newArray(final int size) {
            return new SimpleDate[size];
        }
    };

    public static final JsonDeserializer<SimpleDate> JSON_DESERIALIZER = new JsonDeserializer<SimpleDate>() {
        @Override
        public SimpleDate deserialize(final JsonElement json, final Type typeOfT,
                final JsonDeserializationContext context) throws JsonParseException {
            return fromJson(json);
        }
    };

}

package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class SimpleDate implements Parcelable {

    private static final SimpleDateFormat[] FORMATS = {
            new SimpleDateFormat("MM/dd/yy", Locale.US)
    };

    private final Date mDate;
    private Integer mMonth;
    private Integer mYear;
    private String mDateString;


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

        try {
            return new SimpleDate(Long.parseLong(jsonString));
        } catch (final NumberFormatException e) {
            // this Exception can be safely ignored
        }

        throw new JsonParseException("unable to parse date: " + json);
    }

    @Nullable
    public static JsonElement toJson(@Nullable final SimpleDate date) throws JsonParseException {
        if (date == null) {
            return null;
        } else {
            return new JsonPrimitive(date.getDate().getTime());
        }
    }

    public SimpleDate() {
        this(new Date());
    }

    public SimpleDate(final Date date) {
        mDate = date;
    }

    public SimpleDate(final long date) {
        this(new Date(date));
    }

    private SimpleDate(final Parcel source) {
        this(source.readLong());
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof SimpleDate && mDate.equals(((SimpleDate) obj).getDate());
    }

    public Date getDate() {
        return mDate;
    }

    public String getDateString() {
        if (mDateString == null) {
            mDateString = FORMATS[0].format(mDate);
        }

        return mDateString;
    }

    public int getMonth() {
        if (mMonth == null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(mDate);
            mMonth = calendar.get(Calendar.MONTH);
        }

        return mMonth;
    }

    public int getYear() {
        if (mYear == null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(mDate);
            mYear = calendar.get(Calendar.YEAR);
        }

        return mYear;
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

    public static final Comparator<SimpleDate> CHRONOLOGICAL_ORDER = new Comparator<SimpleDate>() {
        @Override
        public int compare(final SimpleDate o1, final SimpleDate o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    };

    public static final Comparator<SimpleDate> REVERSE_CHRONOLOGICAL_ORDER = new Comparator<SimpleDate>() {
        @Override
        public int compare(final SimpleDate o1, final SimpleDate o2) {
            return CHRONOLOGICAL_ORDER.compare(o2, o1);
        }
    };

    public static final JsonDeserializer<SimpleDate> JSON_DESERIALIZER = new JsonDeserializer<SimpleDate>() {
        @Override
        public SimpleDate deserialize(final JsonElement json, final Type typeOfT,
                final JsonDeserializationContext context) throws JsonParseException {
            return fromJson(json);
        }
    };

    public static final JsonSerializer<SimpleDate> JSON_SERIALIZER = new JsonSerializer<SimpleDate>() {
        @Override
        public JsonElement serialize(final SimpleDate src, final Type typeOfSrc,
                final JsonSerializationContext context) {
            return toJson(src);
        }
    };

}

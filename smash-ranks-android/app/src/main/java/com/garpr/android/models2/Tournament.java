package com.garpr.android.models2;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.ListUtils.AlphabeticallyComparable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Tournament implements AlphabeticallyComparable, Cloneable, Parcelable {


    private static final SimpleDateFormat DATE_PARSER;
    private static final SimpleDateFormat DAY_OF_MONTH_FORMATTER;
    private static final SimpleDateFormat MONTH_FORMATTER;
    private static final SimpleDateFormat YEAR_FORMATTER;

    private final long mTime;
    private final String mDate;
    private final String mDay;
    private final String mId;
    private final String mMonth;
    private final String mMonthAndYear;
    private final String mName;
    private final String mYear;




    static {
        final Locale locale = Locale.getDefault();
        DATE_PARSER = new SimpleDateFormat(Constants.TOURNAMENT_DATE_FORMAT, locale);
        DAY_OF_MONTH_FORMATTER = new SimpleDateFormat(Constants.DAY_OF_MONTH_FORMAT, locale);
        MONTH_FORMATTER = new SimpleDateFormat(Constants.MONTH_FORMAT, locale);
        YEAR_FORMATTER = new SimpleDateFormat(Constants.YEAR_FORMAT, locale);
    }


    public Tournament(final String date, final String id, final String name) {
        mDate = date;
        mId = id;
        mName = name;

        try {
            final Date d = DATE_PARSER.parse(mDate);
            mDay = DAY_OF_MONTH_FORMATTER.format(d);
            mMonth = MONTH_FORMATTER.format(d);
            mYear = YEAR_FORMATTER.format(d);

            mTime = d.getTime();

            final Context context = App.getContext();
           mMonthAndYear = context.getString(R.string.x_y, mMonth, mYear);
        } catch (final ParseException e) {
            throw new RuntimeException(e);
        }
    }


    private Tournament(final Parcel source) {
        mTime = source.readLong();
        mDate = source.readString();
        mDay = source.readString();
        mId = source.readString();
        mMonth = source.readString();
        mMonthAndYear = source.readString();
        mName = source.readString();
        mYear = source.readString();
    }


    @Override
    @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
    public Tournament clone() {
        try {
            return (Tournament) super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }


    @Override
    public boolean equals(final Object o) {
        final boolean isEqual;

        if (this == o) {
            isEqual = true;
        } else if (o instanceof Tournament) {
            final Tournament t = (Tournament) o;
            isEqual = mDate.equals(t.getDate()) && mId.equals(t.getId()) &&
                    mName.equals(t.getName());
        } else {
            isEqual = false;
        }

        return isEqual;
    }


    @Override
    public char getFirstCharOfName() {
        return mName.charAt(0);
    }


    public String getDate() {
        return mDate;
    }


    public String getDay() {
        return mDay;
    }


    public String getId() {
        return mId;
    }


    public String getMonth() {
        return mMonth;
    }


    public String getMonthAndYear() {
        return mMonthAndYear;
    }


    public String getName() {
        return mName;
    }


    public long getTime() {
        return mTime;
    }


    public String getYear() {
        return mYear;
    }


    @Override
    public String toString() {
        return getName();
    }




    /*
     * Code necessary for the Android Parcelable interface is below. Read more here:
     * https://developer.android.com/intl/es/reference/android/os/Parcelable.html
     */


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(mTime);
        dest.writeString(mDate);
        dest.writeString(mDay);
        dest.writeString(mId);
        dest.writeString(mMonth);
        dest.writeString(mMonthAndYear);
        dest.writeString(mName);
        dest.writeString(mYear);
    }


    public static final Creator<Tournament> CREATOR = new Creator<Tournament>() {
        @Override
        public Tournament createFromParcel(final Parcel source) {
            return new Tournament(source);
        }


        @Override
        public Tournament[] newArray(final int size) {
            return new Tournament[size];
        }
    };


}

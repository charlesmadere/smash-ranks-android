package com.garpr.android.models;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.ListUtils.MonthlyComparable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public abstract class BaseDateWrapper implements MonthlyComparable, Parcelable {


    private static final SimpleDateFormat DAY_OF_MONTH_FORMATTER;
    private static final SimpleDateFormat MM_DD_YY_FORMATTER;
    private static final SimpleDateFormat MONTH_FORMATTER;
    private static final SimpleDateFormat YEAR_FORMATTER;

    private final Date mDate;
    private final String mDay;
    private final String mMmDdYy;
    private final String mMonth;
    private final String mMonthAndYear;
    private final String mYear;




    static {
        final Locale locale = Locale.getDefault();
        DAY_OF_MONTH_FORMATTER = new SimpleDateFormat(Constants.DAY_OF_MONTH_FORMAT, locale);
        MM_DD_YY_FORMATTER = new SimpleDateFormat(Constants.MM_DD_YY_FORMAT,locale);
        MONTH_FORMATTER = new SimpleDateFormat(Constants.MONTH_FORMAT, locale);
        YEAR_FORMATTER = new SimpleDateFormat(Constants.YEAR_FORMAT, locale);
    }


    protected BaseDateWrapper(final SimpleDateFormat parser, final String date)
            throws ParseException {
        mDate = parser.parse(date);
        mDay = DAY_OF_MONTH_FORMATTER.format(mDate);
        mMmDdYy = MM_DD_YY_FORMATTER.format(mDate);
        mMonth = MONTH_FORMATTER.format(mDate);
        mYear = YEAR_FORMATTER.format(mDate);

        final Context context = App.get();
        mMonthAndYear = context.getString(R.string.x_y, mMonth, mYear);
    }


    protected BaseDateWrapper(final Parcel source) {
        mDate = new Date(source.readLong());
        mDay = source.readString();
        mMmDdYy = source.readString();
        mMonth = source.readString();
        mMonthAndYear = source.readString();
        mYear = source.readString();
    }


    @Override
    public boolean equals(final Object o) {
        final boolean isEqual;

        if (this == o) {
            isEqual = true;
        } else if (o instanceof BaseDateWrapper) {
            final BaseDateWrapper dw = (BaseDateWrapper) o;
            isEqual = mDate.equals(dw.getDate());
        } else {
            isEqual = false;
        }

        return isEqual;
    }


    public Date getDate() {
        return mDate;
    }


    @Override
    public BaseDateWrapper getDateWrapper() {
        return this;
    }


    public String getDay() {
        return mDay;
    }


    public String getMonth() {
        return mMonth;
    }


    public String getMonthAndYear() {
        return mMonthAndYear;
    }


    public String getMmDdYy() {
        return mMmDdYy;
    }


    public String getYear() {
        return mYear;
    }


    @Override
    public int hashCode() {
        // this method was automatically generated by Android Studio

        int result = mDate.hashCode();
        result = 31 * result + mDay.hashCode();
        result = 31 * result + mMmDdYy.hashCode();
        result = 31 * result + mMonth.hashCode();
        result = 31 * result + mMonthAndYear.hashCode();
        result = 31 * result + mYear.hashCode();

        return result;
    }


    @Override
    public String toString() {
        return getMmDdYy();
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
        dest.writeLong(mDate.getTime());
        dest.writeString(mDay);
        dest.writeString(mMmDdYy);
        dest.writeString(mMonth);
        dest.writeString(mMonthAndYear);
        dest.writeString(mYear);
    }


}

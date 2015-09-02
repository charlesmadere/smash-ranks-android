package com.garpr.android.misc;


import com.crashlytics.android.Crashlytics;


public final class CrashlyticsManager {


    public static void log(final int priority, final String tag, final String msg) {
        throwIfInvalid(tag);
        Crashlytics.log(priority, tag, msg);
    }


    public static void logException(final Throwable throwable) {
        Crashlytics.logException(throwable);
    }


    public static void setBool(final String key, final boolean value) {
        throwIfInvalid(key);
        Crashlytics.setBool(key, value);
    }


    public static void setDouble(final String key, final double value) {
        throwIfInvalid(key);
        Crashlytics.setDouble(key, value);
    }


    public static void setFloat(final String key, final float value) {
        throwIfInvalid(key);
        Crashlytics.setFloat(key, value);
    }


    public static void setInt(final String key, final int value) {
        throwIfInvalid(key);
        Crashlytics.setInt(key, value);
    }


    public static void setLong(final String key, final long value) {
        throwIfInvalid(key);
        Crashlytics.setLong(key, value);
    }


    public static void setString(final String key, final String value) {
        throwIfInvalid(key);
        Crashlytics.setString(key, value);
    }


    public static void setUserIdentifier(final String identifier) {
        Crashlytics.setUserIdentifier(identifier);
    }


    private static void throwIfInvalid(final String key) {
        if (!Utils.validStrings(key)) {
            throw new IllegalArgumentException("key (" + key + ") can't be invalid");
        }
    }


}

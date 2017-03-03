package com.garpr.android.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TimberImpl implements Timber {

    private final CrashlyticsWrapper mCrashlyticsWrapper;
    private final int mMaxSize;
    private final List<Entry> mEntries;


    public TimberImpl(final boolean isLowRamDevice,
            @NonNull final CrashlyticsWrapper crashlyticsWrapper) {
        mMaxSize = isLowRamDevice ? 64 : 256;
        mEntries = new ArrayList<>(mMaxSize);
        mCrashlyticsWrapper = crashlyticsWrapper;
    }

    private void addEntry(final Entry entry) {
        if (mEntries.size() >= mMaxSize) {
            mEntries.remove(mEntries.size() - 1);
        }

        mEntries.add(0, entry);
    }

    @Override
    public synchronized void clearEntries() {
        mEntries.clear();
    }

    @Override
    public synchronized void d(final String tag, final String msg) {
        d(tag, msg, null);
    }

    @Override
    public synchronized void d(final String tag, final String msg, @Nullable final Throwable tr) {
        addEntry(new DebugEntry(tag, msg, tr == null ? null : Log.getStackTraceString(tr)));
        mCrashlyticsWrapper.log(Log.DEBUG, tag, msg);

        if (tr != null) {
            mCrashlyticsWrapper.logException(tr);
        }
    }

    @Override
    public synchronized void e(final String tag, final String msg) {
        e(tag, msg, null);
    }

    @Override
    public synchronized void e(final String tag, final String msg, @Nullable final Throwable tr) {
        addEntry(new ErrorEntry(tag, msg, tr == null ? null : Log.getStackTraceString(tr)));
        mCrashlyticsWrapper.log(Log.ERROR, tag, msg);

        if (tr != null) {
            mCrashlyticsWrapper.logException(tr);
        }
    }

    @NonNull
    @Override
    public synchronized List<Entry> getEntries() {
        return new ArrayList<>(mEntries);
    }

    @Override
    public synchronized void w(final String tag, final String msg) {
        w(tag, msg, null);
    }

    @Override
    public synchronized void w(final String tag, final String msg, @Nullable final Throwable tr) {
        addEntry(new WarnEntry(tag, msg, tr == null ? null : Log.getStackTraceString(tr)));
        mCrashlyticsWrapper.log(Log.WARN, tag, msg);

        if (tr != null) {
            mCrashlyticsWrapper.logException(tr);
        }
    }

}

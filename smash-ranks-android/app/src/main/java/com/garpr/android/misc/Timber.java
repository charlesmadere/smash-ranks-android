package com.garpr.android.misc;

import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.R;

import java.util.List;

public interface Timber {

    void clearEntries();

    void d(final String tag, final String msg);

    void d(final String tag, final String msg, @Nullable final Throwable tr);

    void e(final String tag, final String msg);

    void e(final String tag, final String msg, @Nullable final Throwable tr);

    List<Entry> getEntries();

    void w(final String tag, final String msg);

    void w(final String tag, final String msg, @Nullable final Throwable tr);


    abstract class Entry {
        @AttrRes
        public final int mColorAttrResId;

        @NonNull
        public final String mMsg;

        @Nullable
        public final String mStackTrace;

        @NonNull
        public final String mTag;

        Entry(@AttrRes final int colorAttrResId, @NonNull final String tag,
                @NonNull final String msg, @Nullable final String stackTrace) {
            mColorAttrResId = colorAttrResId;
            mMsg = msg;
            mStackTrace = stackTrace;
            mTag = tag;
        }
    }

    class DebugEntry extends Entry {
        DebugEntry(@NonNull final String tag, @NonNull final String msg,
                @Nullable final String stackTrace) {
            super(R.attr.timberDebug, tag, msg, stackTrace);
        }
    }

    class ErrorEntry extends Entry {
        ErrorEntry(@NonNull final String tag, @NonNull final String msg,
                @Nullable final String stackTrace) {
            super(R.attr.timberError, tag, msg, stackTrace);
        }
    }

    class WarnEntry extends Entry {
        WarnEntry(@NonNull final String tag, @NonNull final String msg,
                @Nullable final String stackTrace) {
            super(R.attr.timberWarn, tag, msg, stackTrace);
        }
    }

}

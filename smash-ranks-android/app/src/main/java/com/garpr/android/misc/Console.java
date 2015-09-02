package com.garpr.android.misc;


import android.util.Log;

import com.garpr.android.BuildConfig;
import com.garpr.android.models.LogMessage;

import java.util.LinkedList;


public final class Console {


    private static final int LOG_MESSAGES_MAX_SIZE;
    private static final LinkedList<LogMessage> LOG_MESSAGES;
    private static long sLogMessageIdPointer;




    static {
        if (BuildConfig.DEBUG) {
            LOG_MESSAGES_MAX_SIZE = 256;
        } else {
            LOG_MESSAGES_MAX_SIZE = 128;
        }

        LOG_MESSAGES = new LinkedList<>();
    }


    private static void add(final int priority, final String tag, final String msg,
            final Throwable tr) {
        final String stackTrace;
        final String throwableMessage;

        CrashlyticsManager.log(priority, tag, msg);

        if (tr == null) {
            stackTrace = null;
            throwableMessage = null;
        } else {
            CrashlyticsManager.logException(tr);
            stackTrace = Log.getStackTraceString(tr);
            throwableMessage = tr.getMessage();
        }

        synchronized (LOG_MESSAGES) {
            LOG_MESSAGES.addFirst(new LogMessage(priority, sLogMessageIdPointer++, tag, msg,
                    stackTrace, throwableMessage));

            while (LOG_MESSAGES.size() > LOG_MESSAGES_MAX_SIZE) {
                LOG_MESSAGES.removeLast();
            }
        }
    }


    public static void clearLogMessages() {
        synchronized (LOG_MESSAGES) {
            LOG_MESSAGES.clear();
        }
    }


    public static void d(final String tag, final String msg) {
        d(tag, msg, null);
    }


    public static void d(final String tag, final String msg, final Throwable tr) {
        add(Log.DEBUG, tag, msg, tr);
    }


    public static void e(final String tag, final String msg) {
        e(tag, msg, null);
    }


    public static void e(final String tag, final String msg, final Throwable tr) {
        add(Log.ERROR, tag, msg, tr);
    }


    public static LogMessage getLogMessage(final int position) {
        synchronized (LOG_MESSAGES) {
            return LOG_MESSAGES.get(position);
        }
    }


    public static int getLogMessagesSize() {
        synchronized (LOG_MESSAGES) {
            return LOG_MESSAGES.size();
        }
    }


    public static boolean hasLogMessages() {
        synchronized (LOG_MESSAGES) {
            return !LOG_MESSAGES.isEmpty();
        }
    }


    public static void w(final String tag, final String msg) {
        w(tag, msg, null);
    }


    public static void w(final String tag, final String msg, final Throwable tr) {
        add(Log.WARN, tag, msg, tr);
    }


}

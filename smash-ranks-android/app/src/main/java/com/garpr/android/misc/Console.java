package com.garpr.android.misc;


import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.app.ActivityManagerCompat;
import android.util.Log;

import com.garpr.android.App;
import com.garpr.android.models.LogMessage;

import java.util.LinkedList;


public final class Console {


    private static final int LOG_MESSAGES_MAX_SIZE;
    private static final LinkedList<LogMessage> LOG_MESSAGES;




    static {
        final Context context = App.get();
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final boolean isLowRamDevice = ActivityManagerCompat.isLowRamDevice(activityManager);

        if (isLowRamDevice) {
            LOG_MESSAGES_MAX_SIZE = 32;
        } else {
            LOG_MESSAGES_MAX_SIZE = 64;
        }

        LOG_MESSAGES = new LinkedList<>();
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
        log(LogMessage.Priority.DEBUG, tag, msg, tr);
    }


    public static void e(final String tag, final String msg) {
        e(tag, msg, null);
    }


    public static void e(final String tag, final String msg, final Throwable tr) {
        log(LogMessage.Priority.ERROR, tag, msg, tr);
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


    private static void log(final LogMessage.Priority priority, final String tag, final String msg,
            final Throwable tr) {
        final String stackTrace;
        final String throwableMessage;

        CrashlyticsManager.log(priority.getCode(), tag, msg);

        if (tr == null) {
            stackTrace = null;
            throwableMessage = null;
        } else {
            CrashlyticsManager.logException(tr);
            stackTrace = Log.getStackTraceString(tr);
            throwableMessage = tr.getMessage();
        }

        synchronized (LOG_MESSAGES) {
            LOG_MESSAGES.addFirst(new LogMessage(priority, tag, msg, stackTrace, throwableMessage));

            while (LOG_MESSAGES.size() > LOG_MESSAGES_MAX_SIZE) {
                LOG_MESSAGES.removeLast();
            }
        }
    }


    public static void w(final String tag, final String msg) {
        w(tag, msg, null);
    }


    public static void w(final String tag, final String msg, final Throwable tr) {
        log(LogMessage.Priority.WARN, tag, msg, tr);
    }


}

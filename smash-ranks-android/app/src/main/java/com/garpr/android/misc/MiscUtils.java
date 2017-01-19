package com.garpr.android.misc;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;

public final class MiscUtils {

    public static Activity getActivity(final Context context) {
        final Activity activity = optActivity(context);

        if (activity == null) {
            throw new NullPointerException("Context (" + context + ") is not attached to an Activity");
        }

        return activity;
    }

    @Nullable
    public static Activity optActivity(@Nullable final Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return optActivity(((ContextWrapper) context).getBaseContext());
        } else {
            return null;
        }
    }

}

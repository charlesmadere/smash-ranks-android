package com.garpr.android.misc;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

import java.text.DecimalFormat;

public final class MiscUtils {

    private static final DecimalFormat DECIMAL_FORMAT;


    static {
        DECIMAL_FORMAT = new DecimalFormat("#.###");
        DECIMAL_FORMAT.setMinimumFractionDigits(3);
    }

    public static Activity getActivity(final Context context) {
        final Activity activity = optActivity(context);

        if (activity == null) {
            throw new NullPointerException("Context (" + context + ") is not attached to an Activity");
        }

        return activity;
    }

    @ColorInt
    public static int getAttrColor(final Context context, @AttrRes final int attrResId)
            throws Resources.NotFoundException {
        final int[] attrs = { attrResId };
        final TypedArray ta = context.obtainStyledAttributes(attrs);

        if (!ta.hasValue(0)) {
            ta.recycle();
            throw new Resources.NotFoundException("unable to find resId (" + attrResId + "): "
                    + context.getResources().getResourceEntryName(attrResId));
        }

        final int color = ta.getColor(0, 0);
        ta.recycle();

        return color;
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

    public static String truncateFloat(final float value) {
        return DECIMAL_FORMAT.format(value);
    }

}

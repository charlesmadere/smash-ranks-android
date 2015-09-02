package com.garpr.android.misc;


import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.garpr.android.App;
import com.garpr.android.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public final class Utils {


    public static final Random RANDOM = new Random();
    private static final String TAG = "Utils";




    public static void applyStatusBarHeight(final View view) {
        final Resources res = view.getResources();
        final int statusBarHeightResId = res.getIdentifier("status_bar_height", "dimen", "android");

        int statusBarHeight;

        if (statusBarHeightResId > 0) {
            try {
                statusBarHeight = res.getDimensionPixelSize(statusBarHeightResId);
            } catch (final Resources.NotFoundException e) {
                Console.w(TAG, "Unable to find Android's status_bar_height resource", e);
                statusBarHeight = res.getDimensionPixelSize(R.dimen.status_bar_height);
            }
        } else {
            statusBarHeight = res.getDimensionPixelSize(R.dimen.status_bar_height);
        }

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = statusBarHeight;
        view.setLayoutParams(params);
    }


    public static boolean areAnyObjectsNull(final Object... objects) {
        if (objects == null || objects.length == 0) {
            return true;
        }

        for (final Object object : objects) {
            if (object == null) {
                return true;
            }
        }

        return false;
    }


    public static String getJSONString(final JSONObject json, final String first,
            final String second) throws JSONException {
        final String string;

        if (json.has(first)) {
            string = json.getString(first);
        } else {
            string = json.getString(second);
        }

        return string;
    }


    public static int googlePlayServicesConnectionStatus() {
        final Context context = App.getContext();
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
    }


    public static boolean googlePlayServicesAreAvailable() {
        final int connectionResult = googlePlayServicesConnectionStatus();
        final boolean googlePlayServicesAreAvailable;

        if (connectionResult == ConnectionResult.SUCCESS) {
            googlePlayServicesAreAvailable = true;
        } else {
            Console.w(TAG, "Google Play Services are unavailable (connectionResult " +
                    connectionResult + ')');
            googlePlayServicesAreAvailable = false;
        }

        return googlePlayServicesAreAvailable;
    }


    public static void hideMenuItems(final MenuItem... items) {
        setMenuItemsVisibility(false, items);
    }


    public static void onGlobalLayout(final View view, final Runnable runnable) {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onGlobalLayout() {
                final ViewTreeObserver vto = view.getViewTreeObserver();

                if (vto.isAlive()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        vto.removeOnGlobalLayoutListener(this);
                    } else {
                        vto.removeGlobalOnLayoutListener(this);
                    }
                }

                runnable.run();
            }
        });
    }


    private static void setMenuItemsVisibility(final boolean visible, final MenuItem... items) {
        if (items != null && items.length >= 1) {
            for (final MenuItem item : items) {
                if (item != null) {
                    item.setVisible(visible);
                }
            }
        }
    }


    public static void showMenuItems(final MenuItem... items) {
        setMenuItemsVisibility(true, items);
    }


    /**
     * Checks a collection of {@link CharSequence} objects to see if any single one of them is
     * null, empty, or just whitespace.
     *
     * @param sequences
     * The collection of {@link CharSequence} objects to check.
     *
     * @return
     * Returns false if any single one of the given {@link CharSequence} objects is null, empty,
     * or just whitespace.
     */
    public static boolean validStrings(final CharSequence... sequences) {
        if (sequences == null || sequences.length == 0) {
            return false;
        }

        for (final CharSequence sequence : sequences) {
            if (TextUtils.isEmpty(sequence) || TextUtils.getTrimmedLength(sequence) == 0) {
                return false;
            }
        }

        return true;
    }


}

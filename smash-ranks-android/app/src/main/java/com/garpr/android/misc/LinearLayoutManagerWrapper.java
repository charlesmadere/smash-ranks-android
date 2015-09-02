package com.garpr.android.misc;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.accessibility.AccessibilityEvent;


/**
 * Gross annoying workaround class for what appears to be a strange, device-specific, crasher.
 * http://stackoverflow.com/questions/29241676/android-recyclerview-last-item-remove-runtime-error
 */
public final class LinearLayoutManagerWrapper extends LinearLayoutManager {


    private static final String TAG = "LinearLayoutManagerWrapper";




    public LinearLayoutManagerWrapper(final Context context) {
        super(context);
    }


    @Override
    public void onInitializeAccessibilityEvent(final AccessibilityEvent event) {
        try {
            super.onInitializeAccessibilityEvent(event);
        } catch (final NullPointerException e) {
            Console.e(TAG, "NullPointerException in onInitializeAccessibilityEvent(" + event +
                    ')', e);
        }
    }


    @Override
    public void onInitializeAccessibilityEvent(final RecyclerView.Recycler recycler,
            final RecyclerView.State state, final AccessibilityEvent event) {
        try {
            super.onInitializeAccessibilityEvent(recycler, state, event);
        } catch (final NullPointerException e) {
            Console.e(TAG, "NullPointerException in onInitializeAccessibilityEvent(" + recycler +
                    ", " + state + ", " + event + ')', e);
        }
    }


}

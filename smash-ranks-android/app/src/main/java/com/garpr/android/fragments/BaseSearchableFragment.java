package com.garpr.android.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.Searchable;

public abstract class BaseSearchableFragment extends BaseFragment implements Searchable {

    @Nullable
    private Listener mListener;


    @Nullable
    protected CharSequence getSearchQuery() {
        return mListener == null ? null : mListener.getSearchQuery();
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        final Activity activity = MiscUtils.optActivity(context);
        if (activity instanceof Listener) {
            mListener = (Listener) activity;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        @Nullable
        CharSequence getSearchQuery();
    }

}

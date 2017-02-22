package com.garpr.android.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.SearchQueryHandle;
import com.garpr.android.misc.Searchable;

public abstract class BaseSearchableFragment extends BaseFragment implements Searchable,
        SearchQueryHandle {

    @Nullable
    private SearchQueryHandle mSearchQueryHandle;


    @Nullable
    public CharSequence getSearchQuery() {
        return mSearchQueryHandle == null ? null : mSearchQueryHandle.getSearchQuery();
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        final Activity activity = MiscUtils.optActivity(context);
        if (activity instanceof SearchQueryHandle) {
            mSearchQueryHandle = (SearchQueryHandle) activity;
        }
    }

    @Override
    public void onDetach() {
        mSearchQueryHandle = null;
        super.onDetach();
    }

}

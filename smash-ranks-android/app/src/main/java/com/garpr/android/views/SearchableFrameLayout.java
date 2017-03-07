package com.garpr.android.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;

import com.garpr.android.App;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.SearchQueryHandle;
import com.garpr.android.misc.Searchable;
import com.garpr.android.misc.ThreadUtils;

import javax.inject.Inject;

public abstract class SearchableFrameLayout extends LifecycleFrameLayout implements Searchable,
        SearchQueryHandle {

    @Inject
    protected ThreadUtils mThreadUtils;


    public SearchableFrameLayout(@NonNull final Context context,
            @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchableFrameLayout(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchableFrameLayout(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Nullable
    @Override
    public CharSequence getSearchQuery() {
        final Activity activity = MiscUtils.optActivity(getContext());

        if (activity instanceof SearchQueryHandle) {
            return ((SearchQueryHandle) activity).getSearchQuery();
        } else {
            return null;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) {
            return;
        }

        App.get().getAppComponent().inject(this);
    }

}

package com.garpr.android.views.toolbars;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.garpr.android.App;
import com.garpr.android.misc.Timber;

import java.lang.reflect.Field;

import javax.inject.Inject;

public final class ToolbarUtils {

    private static final String FIELD_SUBTITLE_TEXT_VIEW = "mSubtitleTextView";
    private static final String FIELD_TITLE_TEXT_VIEW = "mTitleTextView";
    private static final String TAG = "ToolbarUtils";

    private boolean mSearchedForSubtitleTextView;
    private boolean mSearchedForTitleTextView;

    @Nullable
    private TextView mSubtitleTextView;

    @Nullable
    private TextView mTitleTextView;

    @NonNull
    private final Toolbar mToolbar;

    @Inject
    Timber mTimber;


    ToolbarUtils(@NonNull final Toolbar toolbar) {
        mToolbar = toolbar;
        App.get().getAppComponent().inject(this);
    }

    @Nullable
    private TextView getTextViewFromFieldName(@NonNull final String fieldName) {
        TextView textView = null;

        try {
            final Field field = Toolbar.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            textView = (TextView) field.get(mToolbar);
        } catch (final IllegalAccessException e) {
            mTimber.e(TAG, "IllegalAccessException when trying to grab Toolbar field "
                    + fieldName, e);
        } catch (final NoSuchFieldException e) {
            mTimber.e(TAG, "NoSuchFieldException when trying to grab Toolbar field "
                    + fieldName, e);
        }

        return textView;
    }

    @Nullable
    TextView getSubtitleTextView() {
        if (!mSearchedForSubtitleTextView) {
            mSearchedForSubtitleTextView = true;
            mSubtitleTextView = getTextViewFromFieldName(FIELD_SUBTITLE_TEXT_VIEW);
        }

        return mSubtitleTextView;
    }

    @Nullable
    TextView getTitleTextView() {
        if (!mSearchedForTitleTextView) {
            mSearchedForTitleTextView = true;
            mTitleTextView = getTextViewFromFieldName(FIELD_TITLE_TEXT_VIEW);
        }

        return mTitleTextView;
    }

}

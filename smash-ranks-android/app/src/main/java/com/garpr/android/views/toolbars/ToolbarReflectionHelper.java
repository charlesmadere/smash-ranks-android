package com.garpr.android.views.toolbars;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.garpr.android.App;
import com.garpr.android.misc.Timber;

import java.lang.reflect.Field;

import javax.inject.Inject;

public final class ToolbarReflectionHelper {

    private static final String FIELD_NAME_SUBTITLE_TEXT_COLOR = "mSubtitleTextColor";
    private static final String FIELD_NAME_TITLE_TEXT_COLOR = "mTitleTextColor";
    private static final String TAG = "ToolbarReflectionHelper";

    @Nullable
    private Field mSubtitleTextColor;

    @Nullable
    private Field mTitleTextColor;

    @NonNull
    private final Toolbar mToolbar;

    @Inject
    Timber mTimber;


    public ToolbarReflectionHelper(@NonNull final Toolbar toolbar) {
        App.get().getAppComponent().inject(this);
        mToolbar = toolbar;
    }

    @NonNull
    private Field getTextColorField(@NonNull final String fieldName) {
        try {
            final Field field = Toolbar.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (final NoSuchFieldException e) {
            mTimber.e(TAG, "NoSuchFieldException when trying to grab field " + fieldName, e);
            throw new RuntimeException(e);
        }
    }

    @ColorInt
    public int getSubtitleTextColor() {
        Field subtitleTextColor = mSubtitleTextColor;

        if (subtitleTextColor == null) {
            subtitleTextColor = getTextColorField(FIELD_NAME_SUBTITLE_TEXT_COLOR);
            mSubtitleTextColor = subtitleTextColor;
        }

        try {
            return (int) subtitleTextColor.get(mToolbar);
        } catch (final IllegalAccessException e) {
            mTimber.e(TAG, "IllegalAccessException when trying to return value of "
                    + FIELD_NAME_SUBTITLE_TEXT_COLOR, e);
            throw new RuntimeException(e);
        }
    }

    @ColorInt
    public int getTitleTextColor() {
        Field titleTextColor = mTitleTextColor;

        if (titleTextColor == null) {
            titleTextColor = getTextColorField(FIELD_NAME_TITLE_TEXT_COLOR);
            mTitleTextColor = titleTextColor;
        }

        try {
            return (int) titleTextColor.get(mToolbar);
        } catch (final IllegalAccessException e) {
            mTimber.e(TAG, "IllegalAccessException when trying to return value of "
                    + FIELD_NAME_TITLE_TEXT_COLOR, e);
            throw new RuntimeException(e);
        }
    }

}

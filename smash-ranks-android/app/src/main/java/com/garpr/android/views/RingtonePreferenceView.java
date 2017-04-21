package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.preferences.RankingsPollingPreferenceStore;

import javax.inject.Inject;

public class RingtonePreferenceView extends SimplePreferenceView implements View.OnClickListener {

    @Inject
    RankingsPollingPreferenceStore mRankingsPollingPreferenceStore;


    public RingtonePreferenceView(@NonNull final Context context,
            @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public RingtonePreferenceView(@NonNull final Context context,
            @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RingtonePreferenceView(@NonNull final Context context,
            @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr,
            @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }


        refresh();
    }

    @Override
    public void onClick(final View v) {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (!isInEditMode()) {
            App.get().getAppComponent().inject(this);
        }

        setOnClickListener(this);
        setTitleText(R.string.ringtone);

        if (isInEditMode()) {
            return;
        }

        refresh();
    }

    @Override
    public void refresh() {
        super.refresh();


    }

}

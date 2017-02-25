package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.models.SimpleDate;
import com.garpr.android.preferences.Preference;
import com.garpr.android.preferences.RankingsPollingPreferenceStore;

import javax.inject.Inject;

public class LastPollPreferenceView extends SimplePreferenceView implements
        Preference.OnPreferenceChangeListener<SimpleDate> {

    @Inject
    RankingsPollingPreferenceStore mRankingsPollingPreferenceStore;


    public LastPollPreferenceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public LastPollPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LastPollPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }

        mRankingsPollingPreferenceStore.getLastPoll().addListener(this);
        refresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mRankingsPollingPreferenceStore.getLastPoll().removeListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (!isInEditMode()) {
            App.get().getAppComponent().inject(this);
            mRankingsPollingPreferenceStore.getLastPoll().addListener(this);
        }

        setEnabled(false);
        setTitleText(R.string.last_poll);

        if (isInEditMode()) {
            return;
        }

        refresh();
    }

    @Override
    public void onPreferenceChange(final Preference<SimpleDate> preference) {
        if (ViewCompat.isAttachedToWindow(this)) {
            refresh();
        }
    }

    public void refresh() {
        final SimpleDate date = mRankingsPollingPreferenceStore.getLastPoll().get();

        if (date == null) {
            setDescriptionText(R.string.poll_has_yet_to_occur);
        } else {
            setDescriptionText(date.getRelativeDateTimeText(getContext()));
        }
    }

}

package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.models.PollFrequency;
import com.garpr.android.preferences.Preference;
import com.garpr.android.preferences.RankingsPollingPreferenceStore;

import javax.inject.Inject;

public class PollFrequencyPreferenceView extends SimplePreferenceView implements
        DialogInterface.OnClickListener, Preference.OnPreferenceChangeListener<PollFrequency>,
        View.OnClickListener {

    @Inject
    RankingsPollingPreferenceStore mRankingsPollingPreferenceStore;


    public PollFrequencyPreferenceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public PollFrequencyPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PollFrequencyPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }

        mRankingsPollingPreferenceStore.getPollFrequency().addListener(this);
        refresh();
    }

    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        dialog.dismiss();

        final PollFrequency current = mRankingsPollingPreferenceStore.getPollFrequency().get();
        final PollFrequency selected = PollFrequency.values()[which];

        if (current == selected) {
            return;
        }

        mRankingsPollingPreferenceStore.getPollFrequency().set(selected);
        refresh();
    }

    @Override
    public void onClick(final View v) {
        final CharSequence[] items = new CharSequence[PollFrequency.values().length];

        for (int i = 0; i < PollFrequency.values().length; ++i) {
            items[i] = getResources().getText(PollFrequency.values()[i].getTextResId());
        }

        final PollFrequency current = mRankingsPollingPreferenceStore.getPollFrequency().get();
        final int checkedItem = current == null ? -1 : current.ordinal();

        new AlertDialog.Builder(getContext())
                .setSingleChoiceItems(items, checkedItem, this)
                .setTitle(R.string.poll_frequency)
                .show();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mRankingsPollingPreferenceStore.getPollFrequency().removeListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (!isInEditMode()) {
            App.get().getAppComponent().inject(this);
            mRankingsPollingPreferenceStore.getPollFrequency().addListener(this);
        }

        setOnClickListener(this);
        setTitleText(R.string.poll_frequency);

        if (isInEditMode()) {
            return;
        }

        refresh();
    }

    @Override
    public void onPreferenceChange(final Preference<PollFrequency> preference) {
        if (ViewCompat.isAttachedToWindow(this)) {
            refresh();
        }
    }

    @Override
    public void refresh() {
        super.refresh();

        final PollFrequency pollFrequency = mRankingsPollingPreferenceStore.getPollFrequency().get();
        setDescriptionText(pollFrequency == null ? R.string.not_yet_set : pollFrequency.getTextResId());
    }

}

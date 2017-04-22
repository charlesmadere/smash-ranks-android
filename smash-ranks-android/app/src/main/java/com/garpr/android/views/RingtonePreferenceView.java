package com.garpr.android.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.ResultCodes;
import com.garpr.android.misc.Timber;
import com.garpr.android.preferences.RankingsPollingPreferenceStore;

import javax.inject.Inject;

public class RingtonePreferenceView extends SimplePreferenceView implements View.OnClickListener {

    private static final String TAG = "RingtonePreferenceView";

    private RingtoneManager mRingtoneManager;

    @Inject
    RankingsPollingPreferenceStore mRankingsPollingPreferenceStore;

    @Inject
    Timber mTimber;


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

    public void onActivityResult(@Nullable final Intent data) {
        if (data == null || !data.hasExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)) {
            return;
        }

        final Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

        if (pickedUri == null) {
            return;
        }


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
        final Context context = getContext();
        final Activity activity = MiscUtils.getActivity(context);

        final Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
                .putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);

        try {
            activity.startActivityForResult(intent, ResultCodes.RINGTONE_SELECTED.mValue);
        } catch (final ActivityNotFoundException e) {
            mTimber.e(TAG, "Unable to start ringtone picker Activity", e);
            Toast.makeText(context, R.string.unable_to_launch_ringtone_picker, Toast.LENGTH_LONG)
                    .show();
        }
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
        mRingtoneManager = new RingtoneManager(MiscUtils.getActivity(getContext()));

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

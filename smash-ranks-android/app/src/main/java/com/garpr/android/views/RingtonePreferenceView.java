package com.garpr.android.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.ResultCodes;
import com.garpr.android.misc.Timber;
import com.garpr.android.preferences.Preference;
import com.garpr.android.preferences.RankingsPollingPreferenceStore;

import javax.inject.Inject;

public class RingtonePreferenceView extends SimplePreferenceView implements
        Preference.OnPreferenceChangeListener<Uri>, View.OnClickListener {

    private static final String TAG = "RingtonePreferenceView";

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
        mRankingsPollingPreferenceStore.getRingtone().set(pickedUri);
        refresh();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }

        mRankingsPollingPreferenceStore.getRingtone().addListener(this);
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

        final Uri existingUri = mRankingsPollingPreferenceStore.getRingtone().get();

        if (existingUri != null) {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, existingUri);
        }

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
        mRankingsPollingPreferenceStore.getRingtone().removeListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (!isInEditMode()) {
            App.get().getAppComponent().inject(this);
            mRankingsPollingPreferenceStore.getRingtone().addListener(this);
        }

        setOnClickListener(this);
        setTitleText(R.string.ringtone);

        if (isInEditMode()) {
            return;
        }

        refresh();
    }

    @Override
    public void onPreferenceChange(final Preference<Uri> preference) {
        if (ViewCompat.isAttachedToWindow(this)) {
            refresh();
        }
    }

    @Override
    public void refresh() {
        super.refresh();

        final Uri ringtoneUri = mRankingsPollingPreferenceStore.getRingtone().get();
        final Ringtone ringtone = ringtoneUri == null ? null : RingtoneManager.getRingtone(
                getContext(), ringtoneUri);

        if (ringtone == null) {
            setDescriptionText(R.string.none);
        } else {
            setDescriptionText(ringtone.getTitle(getContext()));
        }
    }

}

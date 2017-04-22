package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.activities.HomeActivity;
import com.garpr.android.misc.Timber;
import com.garpr.android.models.NightMode;
import com.garpr.android.preferences.GeneralPreferenceStore;
import com.garpr.android.preferences.Preference;

import javax.inject.Inject;

public class ThemePreferenceView extends SimplePreferenceView implements
        DialogInterface.OnClickListener, DialogInterface.OnDismissListener,
        Preference.OnPreferenceChangeListener<NightMode>, View.OnClickListener {

    private static final String TAG = "ThemePreferenceView";

    @Inject
    GeneralPreferenceStore mGeneralPreferenceStore;

    @Inject
    Timber mTimber;


    public ThemePreferenceView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public ThemePreferenceView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ThemePreferenceView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }

        mGeneralPreferenceStore.getNightMode().addListener(this);
        refresh();
    }

    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        dialog.dismiss();

        final NightMode current = mGeneralPreferenceStore.getNightMode().get();
        final NightMode selected = NightMode.values()[which];

        if (current == selected) {
            return;
        }

        mTimber.d(TAG, "Theme was \"" + current + "\", is now \"" + selected + "\"");

        showRestartDialog();
        mGeneralPreferenceStore.getNightMode().set(selected);
        refresh();
    }

    @Override
    public void onClick(final View v) {
        final CharSequence[] items = new CharSequence[NightMode.values().length];

        for (int i = 0; i < NightMode.values().length; ++i) {
            items[i] = getResources().getText(NightMode.values()[i].getTextResId());
        }

        final NightMode current = mGeneralPreferenceStore.getNightMode().get();
        final int checkedItem = current == null ? -1 : current.ordinal();

        new AlertDialog.Builder(getContext())
                .setSingleChoiceItems(items, checkedItem, this)
                .setTitle(R.string.theme)
                .show();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mGeneralPreferenceStore.getNightMode().removeListener(this);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        final Context context = getContext();
        context.startActivity(HomeActivity.getRestartLaunchIntent(context));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (!isInEditMode()) {
            App.get().getAppComponent().inject(this);
            mGeneralPreferenceStore.getNightMode().addListener(this);
        }

        setOnClickListener(this);
        setTitleText(R.string.theme);

        if (isInEditMode()) {
            return;
        }

        refresh();
    }

    @Override
    public void onPreferenceChange(final Preference<NightMode> preference) {
        if (ViewCompat.isAttachedToWindow(this)) {
            refresh();
        }
    }

    @Override
    public void refresh() {
        super.refresh();

        final NightMode nightMode = mGeneralPreferenceStore.getNightMode().get();
        setDescriptionText(nightMode == null ? R.string.not_yet_set : nightMode.getTextResId());
    }

    private void showRestartDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.the_app_will_now_restart)
                .setNeutralButton(R.string.ok, null)
                .setOnDismissListener(this)
                .show();
    }

}

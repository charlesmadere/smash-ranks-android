package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.BuildConfig;
import com.garpr.android.R;
import com.garpr.android.misc.NotificationManager;

import javax.inject.Inject;

public class TestNotificationView extends SimplePreferenceView implements
        DialogInterface.OnClickListener, View.OnClickListener {

    @Inject
    NotificationManager mNotificationManager;


    public TestNotificationView(@NonNull final Context context,
            @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public TestNotificationView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TestNotificationView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        dialog.dismiss();

        switch (which) {
            case 0:
                mNotificationManager.cancelAll();
                break;

            case 1:
                mNotificationManager.showRankingsUpdated(getContext());
                break;

            default:
                throw new RuntimeException("illegal which: " + which);
        }
    }

    @Override
    public void onClick(final View v) {
        final CharSequence[] items = new CharSequence[] {
                getResources().getText(R.string.cancel_all),
                getResources().getText(R.string.show)
        };

        new AlertDialog.Builder(getContext())
                .setItems(items, this)
                .show();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (!isInEditMode()) {
            App.get().getAppComponent().inject(this);
        }

        setOnClickListener(this);
        setTitleText(R.string.show_test_notification);
        setDescriptionText(R.string.debug_only);
        setVisibility(BuildConfig.DEBUG ? VISIBLE : GONE);
    }

}

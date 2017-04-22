package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
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

    private CharSequence[] mDialogListItems;

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
                showTestNotification();
                break;

            default:
                throw new RuntimeException("illegal which: " + which);
        }
    }

    @Override
    public void onClick(final View v) {
        new AlertDialog.Builder(getContext())
                .setItems(mDialogListItems, this)
                .setTitle(R.string.test_notifications)
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

        final Resources resources = getResources();
        mDialogListItems = new CharSequence[] {
                resources.getText(R.string.cancel_all),
                resources.getText(R.string.show)
        };
    }

    private void showTestNotification() {
        // TODO
    }

}

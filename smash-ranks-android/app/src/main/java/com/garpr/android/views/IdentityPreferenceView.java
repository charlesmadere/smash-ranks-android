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
import com.garpr.android.activities.SetIdentityActivity;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.models.AbsPlayer;

import javax.inject.Inject;

public class IdentityPreferenceView extends SimplePreferenceView implements
        DialogInterface.OnClickListener, IdentityManager.OnIdentityChangeListener,
        View.OnClickListener {

    @Inject
    IdentityManager mIdentityManager;


    public IdentityPreferenceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public IdentityPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IdentityPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }

        mIdentityManager.addListener(this);
        refresh();
    }

    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        mIdentityManager.setIdentity(null);
    }

    @Override
    public void onClick(final View v) {
        final Context context = getContext();

        if (mIdentityManager.hasIdentity()) {
            new AlertDialog.Builder(context)
                    .setMessage(R.string.are_you_sure_you_want_to_delete_your_identity)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.yes, this)
                    .show();
        } else {
            context.startActivity(SetIdentityActivity.getLaunchIntent(context));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIdentityManager.removeListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (!isInEditMode()) {
            App.get().getAppComponent().inject(this);
        }

        setOnClickListener(this);
        setTitleText(R.string.set_your_identity);
        setDescriptionText(R.string.easily_find_yourself_throughout_the_app);

        if (isInEditMode()) {
            return;
        }

        mIdentityManager.addListener(this);
        refresh();
    }

    @Override
    public void onIdentityChange(final IdentityManager identityManager) {
        if (ViewCompat.isAttachedToWindow(this)) {
            refresh();
        }
    }

    @Override
    public void refresh() {
        super.refresh();

        final AbsPlayer player = mIdentityManager.getIdentity();

        if (player == null) {
            setTitleText(R.string.set_your_identity);
            setDescriptionText(R.string.easily_find_yourself_throughout_the_app);
        } else {
            setTitleText(R.string.delete_identity);
            setDescriptionText(player.getName());
        }
    }

}

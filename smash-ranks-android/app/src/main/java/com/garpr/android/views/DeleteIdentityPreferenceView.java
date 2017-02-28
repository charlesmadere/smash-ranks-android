package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.models.AbsPlayer;

import javax.inject.Inject;

public class DeleteIdentityPreferenceView extends SimplePreferenceView implements
        DialogInterface.OnClickListener, IdentityManager.OnIdentityChangeListener,
        View.OnClickListener {

    @Inject
    IdentityManager mIdentityManager;


    public DeleteIdentityPreferenceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public DeleteIdentityPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DeleteIdentityPreferenceView(final Context context, final AttributeSet attrs,
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
        mIdentityManager.set(null);
    }

    @Override
    public void onClick(final View v) {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.are_you_sure_you_want_to_delete_your_identity)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes, this)
                .show();
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
        setTitleText(R.string.delete_identity);

        if (isInEditMode()) {
            return;
        }

        mIdentityManager.addListener(this);
        refresh();
    }

    @Override
    public void onIdentityChange(final IdentityManager identityManager) {
        refresh();
    }

    public void refresh() {
        final AbsPlayer player = mIdentityManager.get();

        if (player == null) {
            setDescriptionText(R.string.no_identity_has_been_set);
        } else {
            setDescriptionText(getResources().getString(R.string.identity_is_x, player.getName()));
        }
    }

}

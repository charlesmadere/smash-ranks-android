package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.activities.SetIdentityActivity;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.models.AbsPlayer;

import javax.inject.Inject;

public class SetIdentityPreferenceView extends SimplePreferenceView implements
        IdentityManager.OnIdentityChangeListener, View.OnClickListener {

    @Inject
    IdentityManager mIdentityManager;


    public SetIdentityPreferenceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public SetIdentityPreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SetIdentityPreferenceView(final Context context, final AttributeSet attrs,
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
    public void onClick(final View v) {
        final Context context = getContext();
        context.startActivity(SetIdentityActivity.getLaunchIntent(getContext()));
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
            setDescriptionText(R.string.easily_find_yourself_throughout_the_app);
        } else {
            setDescriptionText(getResources().getString(R.string.identity_is_x, player.getName()));
        }
    }

}

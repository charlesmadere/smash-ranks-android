package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.models.AbsPlayer;

import javax.inject.Inject;

public abstract class IdentityFrameLayout extends FrameLayout implements
        IdentityManager.OnIdentityChangeListener {

    private Drawable mOriginalBackground;

    @Inject
    protected IdentityManager mIdentityManager;


    public IdentityFrameLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public IdentityFrameLayout(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IdentityFrameLayout(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Nullable
    protected AbsPlayer getIdentity() {
        return null;
    }

    @Nullable
    protected String getIdentityId() {
        return null;
    }

    protected void identityIsSomeoneElse() {
        setBackground(mOriginalBackground);
    }

    protected void identityIsUser() {
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.identity_background));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }

        mIdentityManager.addListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIdentityManager.removeListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) {
            return;
        }

        App.get().getAppComponent().inject(this);
        mIdentityManager.addListener(this);

        mOriginalBackground = getBackground();
    }

    @Override
    public void onIdentityChange(final IdentityManager identityManager) {
        if (ViewCompat.isAttachedToWindow(this)) {
            refreshIdentity();
        }
    }

    protected void refreshIdentity() {
        final AbsPlayer identity = getIdentity();

        if (mIdentityManager.isPlayer(identity)) {
            identityIsUser();
            return;
        }

        final String identityId = getIdentityId();

        if (mIdentityManager.isId(identityId)) {
            identityIsUser();
            return;
        }

        identityIsSomeoneElse();
    }

    protected void styleTextViewForSomeoneElse(final TextView view) {
        view.setTypeface(Typeface.DEFAULT);
    }

    protected void styleTextViewForUser(final TextView view) {
        view.setTypeface(Typeface.DEFAULT_BOLD);
    }

}

package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.models.AbsPlayer;

import javax.inject.Inject;

public class DeleteIdentityPreferenceView extends SimplePreferenceView implements View.OnClickListener {

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

        refresh();
    }

    @Override
    public void onClick(final View v) {
        // TODO
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

        refresh();
    }

    public void refresh() {
        if (mIdentityManager.hasIdentity()) {
            final AbsPlayer player = mIdentityManager.get();
            // noinspection ConstantConditions
            setDescriptionText(getResources().getString(R.string.identity_is_x, player.getName()));
        } else {
            setDescriptionText(R.string.no_identity_has_been_set);
        }
    }

}

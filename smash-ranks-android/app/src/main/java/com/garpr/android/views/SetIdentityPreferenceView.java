package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.activities.SetIdentityActivity;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.models.AbsPlayer;

import javax.inject.Inject;

public class SetIdentityPreferenceView extends SimplePreferenceView implements View.OnClickListener {

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

        refresh();
    }

    @Override
    public void onClick(final View v) {
        final Context context = getContext();
        context.startActivity(SetIdentityActivity.getLaunchIntent(getContext()));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (!isInEditMode()) {
            App.get().getAppComponent().inject(this);
        }

        setOnClickListener(this);
        setTitleText(R.string.set_identity);

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
            setDescriptionText(R.string.easily_find_yourself_throughout_the_app);
        }
    }

}

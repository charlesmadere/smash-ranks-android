package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.activities.PlayerActivity;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.models.AbsPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerItemView extends IdentityFrameLayout implements BaseAdapterView<AbsPlayer>,
        View.OnClickListener {

    private AbsPlayer mContent;

    @BindView(R.id.tvName)
    TextView mName;


    public PlayerItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlayerItemView(final Context context, final AttributeSet attrs, final int defStyleAttr,
            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected AbsPlayer getIdentity() {
        return mContent;
    }

    @Override
    protected void identityIsSomeoneElse() {
        super.identityIsSomeoneElse();
        styleTextViewForSomeoneElse(mName);
    }

    @Override
    protected void identityIsUser() {
        super.identityIsUser();
        styleTextViewForUser(mName);
    }

    @Override
    public void onClick(final View v) {
        final Context context = getContext();
        context.startActivity(PlayerActivity.getLaunchIntent(context, mContent));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setOnClickListener(this);
    }

    @Override
    public void setContent(final AbsPlayer content) {
        mContent = content;

        mName.setText(mContent.getName());

        refreshIdentity();
    }

}

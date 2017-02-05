package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.models.FullPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullPlayerItemView extends LinearLayout implements BaseAdapterView<FullPlayer> {

    @BindView(R.id.tvRating)
    TextView mRating;

    @BindView(R.id.tvUnadjusted)
    TextView mUnadjusted;


    public FullPlayerItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public FullPlayerItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FullPlayerItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    public void setContent(final FullPlayer content) {

    }

}

package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.models.Rating;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingItemView extends LinearLayout implements BaseAdapterView<Rating> {

    @BindView(R.id.tvRating)
    TextView mRating;

    @BindView(R.id.tvUnadjusted)
    TextView mUnadjusted;


    public RatingItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public RatingItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatingItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    public void setContent(final Rating content) {
        final Resources res = getResources();
        mRating.setText(res.getString(R.string.rating_x, content.getRatingTruncated()));
        mUnadjusted.setText(res.getString(R.string.unadjusted_x_y, content.getMuTruncated(),
                content.getSigmaTruncated()));
    }

}

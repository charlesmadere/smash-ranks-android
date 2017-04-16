package com.garpr.android.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.misc.PreviousRankUtils;
import com.garpr.android.models.Ranking;

import javax.inject.Inject;

public class PreviousRankView extends AppCompatImageView implements BaseAdapterView<Ranking> {

    @Inject
    PreviousRankUtils mPreviousRankUtils;


    public PreviousRankView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public PreviousRankView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) {
            return;
        }

        App.get().getAppComponent().inject(this);
    }

    @Override
    public void setContent(final Ranking content) {
        final PreviousRankUtils.Info info = mPreviousRankUtils.checkRanking(content);

        if (info == null) {
            setImageDrawable(null);
            setVisibility(INVISIBLE);
            return;
        }

        final int drawableResId;
        final int tintResId;

        switch (info) {
            case DECREASE:
                drawableResId = R.drawable.ic_arrow_downward_white_18dp;
                tintResId = R.color.lose;
                break;

            case INCREASE:
                drawableResId = R.drawable.ic_arrow_upward_white_18dp;
                tintResId = R.color.win;
                break;

            default:
                throw new RuntimeException("unknown item: " + info);
        }

        final Context context = getContext();
        final Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context,
                drawableResId));
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, tintResId));

        setImageDrawable(drawable);
        setVisibility(VISIBLE);
    }

}

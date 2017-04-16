package com.garpr.android.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.garpr.android.R;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.models.Ranking;

public class PreviousRankView extends AppCompatImageView implements BaseAdapterView<Ranking> {

    public PreviousRankView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public PreviousRankView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setContent(final Ranking content) {
        final Integer previousRank = content.getPreviousRank();
        final int rank = content.getRank();

        if (previousRank == null || previousRank == rank) {
            setImageDrawable(null);
            setVisibility(INVISIBLE);
            return;
        }

        final int drawableResId;
        final int tintResId;

        if (previousRank > rank) {
            drawableResId = R.drawable.ic_arrow_downward_white_18dp;
            tintResId = R.color.lose;
        } else {
            drawableResId = R.drawable.ic_arrow_upward_white_18dp;
            tintResId = R.color.win;
        }

        final Context context = getContext();
        final Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context,
                drawableResId));
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, tintResId));

        setImageDrawable(drawable);
        setVisibility(VISIBLE);
    }

}

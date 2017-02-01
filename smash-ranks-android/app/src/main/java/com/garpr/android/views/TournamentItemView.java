package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.models.Tournament;

public class TournamentItemView extends LinearLayout implements BaseAdapterView<Tournament> {

    public TournamentItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public TournamentItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TournamentItemView(final Context context, final AttributeSet attrs, final int defStyleAttr,
            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setContent(final Tournament content) {
        // TODO
    }

}

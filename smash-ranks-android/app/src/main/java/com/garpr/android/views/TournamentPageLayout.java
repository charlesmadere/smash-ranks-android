package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.garpr.android.R;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.models.FullTournament;

import butterknife.BindView;

public abstract class TournamentPageLayout extends SearchableFrameLayout implements
        BaseAdapterView<FullTournament> {

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.empty)
    protected View mEmpty;


    public TournamentPageLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public TournamentPageLayout(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TournamentPageLayout(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}

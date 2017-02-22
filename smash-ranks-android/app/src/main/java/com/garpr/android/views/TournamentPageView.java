package com.garpr.android.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.misc.Heartbeat;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.SearchQueryHandle;
import com.garpr.android.misc.Searchable;
import com.garpr.android.misc.ThreadUtils;
import com.garpr.android.models.FullTournament;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class TournamentPageView extends FrameLayout implements
        BaseAdapterView<FullTournament>, Heartbeat, Searchable, SearchQueryHandle {

    @Inject
    protected ThreadUtils mThreadUtils;

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.empty)
    protected View mEmpty;


    public TournamentPageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public TournamentPageView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TournamentPageView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Nullable
    @Override
    public CharSequence getSearchQuery() {
        final Activity activity = MiscUtils.optActivity(getContext());

        if (activity instanceof SearchQueryHandle) {
            return ((SearchQueryHandle) activity).getSearchQuery();
        } else {
            return null;
        }
    }

    @Override
    public boolean isAlive() {
        return ViewCompat.isAttachedToWindow(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        App.get().getAppComponent().inject(this);
        ButterKnife.bind(this);
    }

}

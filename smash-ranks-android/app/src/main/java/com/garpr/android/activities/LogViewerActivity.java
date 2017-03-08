package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.garpr.android.R;
import com.garpr.android.adapters.TimberEntriesAdapter;
import com.garpr.android.misc.Timber;
import com.garpr.android.views.RefreshLayout;

import java.util.List;

import butterknife.BindView;

public class LogViewerActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "LogViewerActivity";

    private TimberEntriesAdapter mAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    @BindView(R.id.empty)
    View mEmpty;


    public static Intent getLaunchIntent(final Context context) {
        return new Intent(context, LogViewerActivity.class);
    }

    private void fetchTimberEntries() {
        mRefreshLayout.setRefreshing(true);

        final List<Timber.Entry> entries = mTimber.getEntries();

        if (entries.isEmpty()) {
            mAdapter.clear();
            mRecyclerView.setVisibility(View.GONE);
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mAdapter.set(entries);
            mEmpty.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        supportInvalidateOptionsMenu();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_viewer);
        fetchTimberEntries();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_log_viewer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miClearLog:
                mTimber.clearEntries();
                fetchTimberEntries();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        if (mAdapter != null && !mAdapter.isEmpty()) {
            menu.findItem(R.id.miClearLog).setEnabled(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onRefresh() {
        fetchTimberEntries();
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();

        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new TimberEntriesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

}

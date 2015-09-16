package com.garpr.android.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.views.RefreshLayout;

import butterknife.Bind;


public abstract class BaseToolbarListActivity extends BaseToolbarActivity implements
        RefreshLayout.OnRefreshListener {


    @Bind(R.id.activity_base_list_error)
    LinearLayout mErrorView;

    @Bind(R.id.activity_base_list_refresh)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.activity_base_list_list)
    RecyclerView mRecyclerView;

    @Bind(R.id.activity_base_list_error_line)
    TextView mErrorLine;

    private boolean mIsLoading;
    private RecyclerView.Adapter mAdapter;




    @Override
    protected int getContentView() {
        return R.layout.activity_base_list;
    }


    protected String getErrorText() {
        return getString(R.string.error_);
    }


    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }


    protected boolean isLoading() {
        return mIsLoading;
    }


    protected void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readIntentData(getIntent());
        prepareViews();
    }


    @Override
    public void onRefresh() {
        mErrorView.setVisibility(View.GONE);
    }


    protected void prepareViews() {
        mErrorLine.setText(getErrorText());
        mRefreshLayout.setOnRefreshListener(this);
    }


    protected void readIntentData(final Intent intent) {
        // this method intentionally left blank (children can override)
    }


    protected void setAdapter(final RecyclerView.Adapter adapter) {
        mErrorView.setVisibility(View.GONE);
        mAdapter = adapter;
        mAdapter.setHasStableIds(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        setLoading(false);
    }


    protected void setLoading(final boolean isLoading) {
        mIsLoading = isLoading;
        mRefreshLayout.setRefreshing(isLoading);
    }


    protected void showError() {
        setLoading(false);
        mRecyclerView.setAdapter(null);
        mErrorView.setVisibility(View.VISIBLE);
    }


}

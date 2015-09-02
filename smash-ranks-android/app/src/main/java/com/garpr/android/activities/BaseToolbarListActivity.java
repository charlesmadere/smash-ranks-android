package com.garpr.android.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.misc.LinearLayoutManagerWrapper;
import com.garpr.android.misc.RecyclerAdapter;
import com.garpr.android.views.FlexibleSwipeRefreshLayout;


public abstract class BaseToolbarListActivity extends BaseToolbarActivity implements
        SwipeRefreshLayout.OnRefreshListener {


    private boolean mIsLoading;
    private FlexibleSwipeRefreshLayout mRefreshLayout;
    private LinearLayout mErrorView;
    private RecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mErrorLine;




    protected void findViews() {
        mErrorLine = (TextView) findViewById(R.id.activity_base_list_error_line);
        mErrorView = (LinearLayout) findViewById(R.id.activity_base_list_error);
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_base_list_list);
        mRefreshLayout = (FlexibleSwipeRefreshLayout) findViewById(R.id.activity_base_list_refresh);
    }


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
            mAdapter.dataSetChanged();
        }
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readIntentData(getIntent());
        findViews();
        prepareViews();
    }


    @Override
    public void onRefresh() {
        mErrorView.setVisibility(View.GONE);
    }


    protected void prepareViews() {
        mErrorLine.setText(getErrorText());
        mRecyclerView.setLayoutManager(new LinearLayoutManagerWrapper(this));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setScrollingView(mRecyclerView);
    }


    protected void readIntentData(final Intent intent) {
        // this method intentionally left blank (children can override)
    }


    protected void setAdapter(final RecyclerAdapter adapter) {
        mErrorView.setVisibility(View.GONE);
        mAdapter = adapter;
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

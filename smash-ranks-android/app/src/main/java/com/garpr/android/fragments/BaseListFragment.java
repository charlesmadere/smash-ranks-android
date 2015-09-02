package com.garpr.android.fragments;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.views.RefreshLayout;


public abstract class BaseListFragment extends BaseFragment implements
        RefreshLayout.OnRefreshListener {


    private boolean mIsLoading;
    private LinearLayout mErrorView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RefreshLayout mRefreshLayout;
    private TextView mErrorLine;




    protected void findViews() {
        final View view = getView();
        mErrorLine = (TextView) view.findViewById(R.id.fragment_base_list_error_line);
        mErrorView = (LinearLayout) view.findViewById(R.id.fragment_base_list_error);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_base_list_list);
        mRefreshLayout = (RefreshLayout) view.findViewById(R.id.fragment_base_list_refresh);
    }


    @Override
    protected int getContentView() {
        return R.layout.fragment_base_list;
    }


    protected String getErrorText() {
        return getString(R.string.error_);
    }


    public RecyclerView getRecyclerView() {
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
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        readArguments(getArguments());
        findViews();
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


    protected void readArguments(final Bundle arguments) {
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

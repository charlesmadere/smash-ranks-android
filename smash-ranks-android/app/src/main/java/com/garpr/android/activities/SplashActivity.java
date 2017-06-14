package com.garpr.android.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.models.RegionsBundle;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.views.RefreshLayout;

import javax.inject.Inject;

import butterknife.BindView;

public class SplashActivity extends BaseActivity implements ApiListener<RegionsBundle>,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "SplashActivity";

    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    @BindView(R.id.error)
    View mError;

    @BindView(R.id.loading)
    View mLoading;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;


    @Override
    public void failure(final int errorCode) {

    }

    private void fetchDefaultRegion() {
        mRefreshLayout.setRefreshing(true);
        mError.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);
        mServerApi.getRegions(new ApiCall<>(this));
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    private void goToHomeActivity() {
        startActivity(HomeActivity.getLaunchIntent(this));
        supportFinishAfterTransition();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);

        if (mRegionManager.getRegion() == null) {
            setContentView(R.layout.activity_splash);
            fetchDefaultRegion();
        } else {
            goToHomeActivity();
        }
    }

    @Override
    public void onRefresh() {
        fetchDefaultRegion();
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();

        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void success(@Nullable final RegionsBundle regionsBundle) {

    }

}

package com.garpr.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.DeepLinkUtils;
import com.garpr.android.models.Region;
import com.garpr.android.models.RegionsBundle;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;

import javax.inject.Inject;

import butterknife.BindView;

public class DeepLinkActivity extends BaseActivity implements ApiListener<RegionsBundle> {

    private static final String TAG = "DeepLinkActivity";

    private Region mRegion;

    @Inject
    DeepLinkUtils mDeepLinkUtils;

    @Inject
    ServerApi mServerApi;

    @BindView(R.id.error)
    View mError;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;


    private void deepLink() {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(this, getIntent(), mRegion);

        if (intentStack == null || intentStack.length == 0) {
            startActivity(HomeActivity.getLaunchIntent(this));
        } else {
            ContextCompat.startActivities(this, intentStack);
        }

        supportFinishAfterTransition();
    }

    @Override
    public void failure(final int errorCode) {
        // TODO
    }

    private void fetchRegions() {
        mRegion = null;
        mRefreshLayout.setRefreshing(true);
        mServerApi.getRegions(new ApiCall<>(this));
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_deep_link);

        if (!mDeepLinkUtils.isValidUri(getIntent())) {
            startActivity(HomeActivity.getLaunchIntent(this));
            supportFinishAfterTransition();
            return;
        }

        fetchRegions();
    }

    @Override
    public void success(@Nullable final RegionsBundle regionsBundle) {
        // TODO
    }

}

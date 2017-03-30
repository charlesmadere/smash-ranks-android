package com.garpr.android.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.DeepLinkUtils;
import com.garpr.android.models.Endpoint;
import com.garpr.android.models.RegionsBundle;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;

import javax.inject.Inject;

import butterknife.BindView;

public class DeepLinkActivity extends BaseActivity implements ApiListener<RegionsBundle>,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "DeepLinkActivity";

    @Inject
    DeepLinkUtils mDeepLinkUtils;

    @Inject
    ServerApi mServerApi;

    @BindView(R.id.error)
    View mError;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;


    @Override
    public void failure() {

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

        final Endpoint endpoint = mDeepLinkUtils.getEndpoint(this);

        // TODO

        final DeepLinkUtils.Details details = mDeepLinkUtils.buildIntentStack(this);

        if (details == null) {
            startActivity(HomeActivity.getLaunchIntent(this));
        } else {
            ContextCompat.startActivities(this, details.mIntents);
        }

        finish();
    }

    @Override
    public void onRefresh() {

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

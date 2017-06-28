package com.garpr.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.DeepLinkUtils;
import com.garpr.android.models.Region;
import com.garpr.android.models.RegionsBundle;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;

import javax.inject.Inject;

public class DeepLinkActivity extends BaseActivity implements ApiListener<RegionsBundle> {

    private static final String TAG = "DeepLinkActivity";

    @Inject
    DeepLinkUtils mDeepLinkUtils;

    @Inject
    ServerApi mServerApi;


    private void deepLink(final Region region) {
        final Intent[] intentStack = mDeepLinkUtils.buildIntentStack(this, getIntent(), region);

        if (intentStack == null || intentStack.length == 0) {
            startActivity(HomeActivity.Companion.getLaunchIntent(this));
        } else {
            ContextCompat.startActivities(this, intentStack);
        }

        supportFinishAfterTransition();
    }

    private void error() {
        Toast.makeText(this, R.string.error_loading_deep_link_data, Toast.LENGTH_LONG).show();
        startActivity(HomeActivity.Companion.getLaunchIntent(this));
        supportFinishAfterTransition();
    }

    @Override
    public void failure(final int errorCode) {
        error();
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

        if (mDeepLinkUtils.isValidUri(getIntent())) {
            mServerApi.getRegions(new ApiCall<>(this));
        } else {
            error();
        }
    }

    @Override
    public void success(@Nullable final RegionsBundle regionsBundle) {
        if (regionsBundle != null && regionsBundle.hasRegions()) {
            final Region region = mDeepLinkUtils.getRegion(getIntent(), regionsBundle);

            if (region == null) {
                error();
            } else {
                deepLink(region);
            }
        } else {
            error();
        }
    }

}

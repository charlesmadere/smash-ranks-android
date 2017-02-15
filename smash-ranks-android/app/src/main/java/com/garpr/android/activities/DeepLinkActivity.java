package com.garpr.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.DeepLinkUtils;

import javax.inject.Inject;

public class DeepLinkActivity extends BaseActivity {

    private static final String TAG = "DeepLinkActivity";

    @Inject
    DeepLinkUtils mDeepLinkUtils;


    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_deep_link);

        final Intent[] intents = mDeepLinkUtils.buildIntentStack(this);

        if (intents == null || intents.length == 0) {
            startActivity(HomeActivity.getLaunchIntent(this));
        } else {
            ContextCompat.startActivities(this, intents);
        }

        finish();
    }

}

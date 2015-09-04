package com.garpr.android.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.garpr.android.R;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsManager;
import com.garpr.android.settings.Settings;

import java.util.List;


public class UrlHandlerActivity extends BaseActivity {


    private static final String TAG = "UrlHandlerActivity";

    private Button mRetry;
    private LinearLayout mError;
    private ProgressBar mProgress;




    private void findViews() {
        mError = (LinearLayout) findViewById(R.id.activity_url_handler_error);
        mProgress = (ProgressBar) findViewById(R.id.activity_url_handler_progress);
        mRetry = (Button) findViewById(R.id.activity_url_handler_retry);
    }


    @Override
    public String getActivityName() {
        return TAG;
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_url_handler;
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        prepareViews();

        CrashlyticsManager.setBool(Constants.DEEP_LINK, true);

        if (Settings.OnboardingComplete.get()) {
            final Intent intent = getIntent();
            final Uri uri = intent.getData();
            parseUri(uri);
        } else {
            OnboardingActivity.start(this);
        }

        finish();
    }


    private void parseUri(final Uri uri) {
        CrashlyticsManager.setString(Constants.DEEP_LINK_URL, uri.toString());
        final List<String> segments = uri.getPathSegments();

        if (segments == null || segments.isEmpty()) {
            RankingsActivity.start(this);
            return;
        }

        // TODO
        // parse segments

        final String a = segments.get(0);
        final String z = segments.toString();
    }


    private void prepareViews() {
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO
                // refresh
            }
        });
    }


}

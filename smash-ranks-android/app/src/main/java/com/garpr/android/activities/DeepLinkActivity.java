package com.garpr.android.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.garpr.android.R;
import com.garpr.android.misc.Console;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsManager;
import com.garpr.android.settings.Settings;

import java.util.List;


public class DeepLinkActivity extends BaseActivity {


    private static final String TAG = "DeepLinkActivity";

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

        if (Settings.OnboardingComplete.get()) {
            Console.d(TAG, "Attempting to deep link...");

            final Intent intent = getIntent();

            if (intent == null) {
                Console.w(TAG, "Cancelling deep link because Intent is null");
                RankingsActivity.start(this);
            } else {
                final Uri uri = intent.getData();

                if (uri == null) {
                    Console.w(TAG, "Cancelling deep link because Uri is null");
                    RankingsActivity.start(this);
                } else {
                    parseUri(uri);
                }
            }
        } else {
            Console.d(TAG, "Deep link cancelled because onboarding is incomplete");
            OnboardingActivity.start(this);
        }

        finish();
    }


    private void parseUri(final Uri uri) {
        final String uriString = uri.toString();
        CrashlyticsManager.setString(Constants.DEEP_LINK_URL, uriString);
        Console.d(TAG, "Deep link Uri: " + uriString);

        final List<String> segments = uri.getPathSegments();
        // TODO this is always returning null / empty...

        if (segments == null || segments.isEmpty()) {
            Console.w(TAG, "Cancelling deep link because Uri has no segments");
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

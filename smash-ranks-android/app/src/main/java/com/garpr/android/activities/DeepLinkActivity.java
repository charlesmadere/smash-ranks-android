package com.garpr.android.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.garpr.android.R;
import com.garpr.android.calls.Regions;
import com.garpr.android.misc.Console;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsManager;
import com.garpr.android.misc.ResponseOnUi;
import com.garpr.android.misc.Utils;
import com.garpr.android.models.Region;
import com.garpr.android.settings.Settings;

import java.util.ArrayList;


public class DeepLinkActivity extends BaseActivity {


    private static final String TAG = "DeepLinkActivity";

    private Button mRetry;
    private LinearLayout mError;
    private ProgressBar mProgress;




    private void fetchRankings(final String regionId) {
        Regions.get(new ResponseOnUi<ArrayList<Region>>(TAG, this) {
            @Override
            public void errorOnUi(final Exception e) {
                showError();
            }


            @Override
            public void successOnUi(final ArrayList<Region> regions) {
                Region region = null;

                for (final Region r : regions) {
                    if (r.getId().equalsIgnoreCase(regionId)) {
                        region = r;
                        break;
                    }
                }

                if (region != null) {
                    Settings.Region.set(region);
                }

                RankingsActivity.start(DeepLinkActivity.this);
                finish();
            }
        }, false);
    }


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

            if (getIntent() == null) {
                Console.w(TAG, "Cancelling deep link because Intent is null");
                RankingsActivity.start(this);
                finish();
            } else {
                parseIntent();
            }
        } else {
            Console.d(TAG, "Deep link cancelled because onboarding is incomplete");
            OnboardingActivity.start(this);
            finish();
        }
    }


    private void parseIntent() {
        final Intent intent = getIntent();
        final Uri uri = intent.getData();

        if (uri == null) {
            Console.w(TAG, "Cancelling deep link because Uri is null");
            RankingsActivity.start(this);
            finish();
        } else if (parseUri(uri)) {
            // intentionally blank
        } else {
            RankingsActivity.start(this);
            finish();
        }
    }


    private boolean parseUri(final Uri uri) {
        final String uriString = uri.toString();

        if (!Utils.validStrings(uriString)) {
            Console.w(TAG, "Deep link Uri String is invalid");
            return false;
        }

        CrashlyticsManager.setString(Constants.DEEP_LINK_URL, uriString);
        Console.d(TAG, "Deep link Uri: \"" + uriString + '"');

        final String path = uriString.substring(Constants.WEB_URL.length(), uriString.length());

        if (path.contains("/")) {
            // TODO split
            final String[] paths = path.split("/");

        } else {
            fetchRankings(path);
        }

        return true;
    }


    private void prepareViews() {
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Console.d(TAG, "Retrying deep link...");
                showProgress();
                parseIntent();
            }
        });
    }


    private void showError() {
        mProgress.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
    }


    private void showProgress() {
        mError.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }


}

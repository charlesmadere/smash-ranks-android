package com.garpr.android.activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.garpr.android.R;
import com.garpr.android.misc.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;


public class AboutActivity extends BaseToolbarActivity {


    private static final String TAG = "AboutActivity";

    @Bind(R.id.activity_about_webview)
    protected WebView mWebView;




    public static void start(final Activity activity) {
        final Intent intent = new Intent(activity, AboutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
    }


    @Override
    public String getActivityName() {
        return TAG;
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_about;
    }


    @Override
    protected int getSelectedNavigationItemId() {
        return R.id.navigation_view_menu_about;
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        prepareViews();
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void prepareViews() {
        final WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl(Constants.ABOUT_URL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

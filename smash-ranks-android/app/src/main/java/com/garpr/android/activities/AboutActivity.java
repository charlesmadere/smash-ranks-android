package com.garpr.android.activities;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.garpr.android.R;
import com.garpr.android.misc.Constants;


public class AboutActivity extends BaseToolbarActivity {


    private static final String TAG = "AboutActivity";

    private WebView mWebView;




    private void findViews() {
        mWebView = (WebView) findViewById(R.id.activity_about_webview);
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
        findViews();
        prepareViews();
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void prepareViews() {
        final WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl(Constants.ABOUT_URL);
    }




    public static class IntentBuilder extends BaseActivity.IntentBuilder {


        public IntentBuilder(final Context context) {
            super(context, AboutActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }


    }


}

package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.preferences.GeneralPreferenceStore;
import com.garpr.android.preferences.Preference;
import com.garpr.android.preferences.RankingsPollingPreferenceStore;
import com.garpr.android.views.CheckablePreferenceView;
import com.garpr.android.views.LastPollPreferenceView;
import com.garpr.android.views.ThemePreferenceView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity {

    private static final String TAG = "SettingsActivity";

    @Inject
    GeneralPreferenceStore mGeneralPreferenceStore;

    @Inject
    RankingsPollingPreferenceStore mRankingsPollingPreferenceStore;

    @BindView(R.id.cpvMustBeCharging)
    CheckablePreferenceView mMustBeCharging;

    @BindView(R.id.cpvMustBeOnWifi)
    CheckablePreferenceView mMustBeOnWifi;

    @BindView(R.id.cpvUseRankingsPolling)
    CheckablePreferenceView mUseRankingsPolling;

    @BindView(R.id.lastPollPreferenceView)
    LastPollPreferenceView mLastPoll;

    @BindView(R.id.themePreferenceView)
    ThemePreferenceView mTheme;


    public static Intent getLaunchIntent(final Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    public String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRankingsPollingPreferenceStore.getEnabled().removeListener(mOnRankingsPollingEnabledChange);
    }

    @OnClick(R.id.spvLogViewer)
    void onLogViewerClick() {
        startActivity(LogViewerActivity.getLaunchIntent(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();

        mRankingsPollingPreferenceStore.getEnabled().addListener(mOnRankingsPollingEnabledChange);
        mUseRankingsPolling.set(mRankingsPollingPreferenceStore.getEnabled());
        mMustBeOnWifi.set(mRankingsPollingPreferenceStore.getWifiRequired());
        mMustBeCharging.set(mRankingsPollingPreferenceStore.getChargingRequired());
    }

    private void refresh() {
        mTheme.refresh();
        mUseRankingsPolling.refresh();
        mMustBeOnWifi.refresh();
        mMustBeCharging.refresh();
        mLastPoll.refresh();

        if (Boolean.TRUE.equals(mRankingsPollingPreferenceStore.getEnabled().get())) {
            mMustBeOnWifi.setEnabled(true);
            mMustBeCharging.setEnabled(true);
        } else {
            mMustBeOnWifi.setEnabled(false);
            mMustBeCharging.setEnabled(false);
        }
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    private final Preference.OnPreferenceChangeListener<Boolean> mOnRankingsPollingEnabledChange =
            new Preference.OnPreferenceChangeListener<Boolean>() {
        @Override
        public void onPreferenceChange(final Preference<Boolean> preference) {
            refresh();
        }
    };

}

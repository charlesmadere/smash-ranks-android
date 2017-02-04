package com.garpr.android.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.GoogleApiWrapper;
import com.garpr.android.preferences.Preference;
import com.garpr.android.preferences.RankingsPollingPreferenceStore;
import com.garpr.android.views.CheckablePreferenceView;
import com.garpr.android.views.LastPollPreferenceView;
import com.garpr.android.views.PollFrequencyPreferenceView;
import com.garpr.android.views.ThemePreferenceView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity implements DialogInterface.OnCancelListener,
        DialogInterface.OnDismissListener {

    private static final String TAG = "SettingsActivity";

    @Inject
    GoogleApiWrapper mGoogleApiWrapper;

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

    @BindView(R.id.pollFrequencyPreferenceView)
    PollFrequencyPreferenceView mPollFrequency;

    @BindView(R.id.tvGooglePlayServicesError)
    TextView mGooglePlayServicesError;

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
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }

    @Override
    public void onCancel(final DialogInterface dialog) {
        refresh();
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

    @Override
    public void onDismiss(final DialogInterface dialog) {
        refresh();
    }

    @OnClick(R.id.tvGooglePlayServicesError)
    void onGooglePlayServicesErrorClick() {
        final int connectionStatus = mGoogleApiWrapper.getGooglePlayServicesConnectionStatus();

        if (mGoogleApiWrapper.isConnectionStatusSuccess(connectionStatus)) {
            Toast.makeText(this, R.string.google_play_services_error_has_been_resolved,
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (mGoogleApiWrapper.showPlayServicesResolutionDialog(connectionStatus, this, this)) {
            return;
        }

        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.google_play_services_error_cant_be_resolved,
                        connectionStatus))
                .setNeutralButton(R.string.ok, null)
                .setOnDismissListener(this)
                .show();
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
        mPollFrequency.refresh();
        mMustBeOnWifi.refresh();
        mMustBeCharging.refresh();
        mLastPoll.refresh();

        if (mGoogleApiWrapper.isGooglePlayServicesAvailable()) {
            mGooglePlayServicesError.setVisibility(View.GONE);
            mUseRankingsPolling.setEnabled(true);

            if (Boolean.TRUE.equals(mRankingsPollingPreferenceStore.getEnabled().get())) {
                mPollFrequency.setEnabled(true);
                mMustBeOnWifi.setEnabled(true);
                mMustBeCharging.setEnabled(true);
            } else {
                mPollFrequency.setEnabled(false);
                mMustBeOnWifi.setEnabled(false);
                mMustBeCharging.setEnabled(false);
            }
        } else {
            mGooglePlayServicesError.setVisibility(View.VISIBLE);
            mUseRankingsPolling.setEnabled(false);
            mPollFrequency.setEnabled(false);
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

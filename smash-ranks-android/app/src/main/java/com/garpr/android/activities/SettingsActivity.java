package com.garpr.android.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.FavoritePlayersManager;
import com.garpr.android.misc.GoogleApiWrapper;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.ResultCodes;
import com.garpr.android.misc.ShareUtils;
import com.garpr.android.models.PollFrequency;
import com.garpr.android.preferences.Preference;
import com.garpr.android.preferences.RankingsPollingPreferenceStore;
import com.garpr.android.sync.RankingsPollingSyncManager;
import com.garpr.android.views.CheckablePreferenceView;
import com.garpr.android.views.DeleteFavoritePlayersPreferenceView;
import com.garpr.android.views.IdentityPreferenceView;
import com.garpr.android.views.LastPollPreferenceView;
import com.garpr.android.views.PollFrequencyPreferenceView;
import com.garpr.android.views.RegionPreferenceView;
import com.garpr.android.views.RingtonePreferenceView;
import com.garpr.android.views.ThemePreferenceView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity {

    private static final String TAG = "SettingsActivity";

    @Inject
    FavoritePlayersManager mFavoritePlayersManager;

    @Inject
    GoogleApiWrapper mGoogleApiWrapper;

    @Inject
    IdentityManager mIdentityManager;

    @Inject
    RankingsPollingPreferenceStore mRankingsPollingPreferenceStore;

    @Inject
    RankingsPollingSyncManager mRankingsPollingSyncManager;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ShareUtils mShareUtils;

    @BindView(R.id.cpvMustBeCharging)
    CheckablePreferenceView mMustBeCharging;

    @BindView(R.id.cpvMustBeOnWifi)
    CheckablePreferenceView mMustBeOnWifi;

    @BindView(R.id.cpvUseRankingsPolling)
    CheckablePreferenceView mUseRankingsPolling;

    @BindView(R.id.cpvVibrate)
    CheckablePreferenceView mVibrate;

    @BindView(R.id.deleteFavoritePlayersPreferenceView)
    DeleteFavoritePlayersPreferenceView mDeleteFavoritePlayersPreferenceView;

    @BindView(R.id.identityPreferenceView)
    IdentityPreferenceView mIdentityPreferenceView;

    @BindView(R.id.lastPollPreferenceView)
    LastPollPreferenceView mLastPoll;

    @BindView(R.id.pollFrequencyPreferenceView)
    PollFrequencyPreferenceView mPollFrequency;

    @BindView(R.id.regionPreferenceView)
    RegionPreferenceView mRegionPreferenceView;

    @BindView(R.id.ringtonePreferenceView)
    RingtonePreferenceView mRingtonePreferenceView;

    @BindView(R.id.tvGooglePlayServicesError)
    TextView mGooglePlayServicesError;

    @BindView(R.id.themePreferenceView)
    ThemePreferenceView mThemePreferenceView;


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

        if (requestCode == ResultCodes.RINGTONE_SELECTED.mValue) {
            mRingtonePreferenceView.onActivityResult(data);
        }

        refresh();
    }

    @OnClick(R.id.spvCharlesTwitter)
    void onCharlesTwitterClick() {
        mShareUtils.openUrl(this, Constants.CHARLES_TWITTER_URL);
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

        mFavoritePlayersManager.removeListener(mOnFavoritePlayersChangeListener);
        mIdentityManager.removeListener(mOnIdentityChangeListener);
        mRegionManager.removeListener(mOnRegionChangeListener);

        mRankingsPollingPreferenceStore.getChargingRequired().removeListener(mOnChargingRequiredChange);
        mRankingsPollingPreferenceStore.getEnabled().removeListener(mOnRankingsPollingEnabledChange);
        mRankingsPollingPreferenceStore.getPollFrequency().removeListener(mOnPollFrequencyChange);
        mRankingsPollingPreferenceStore.getRingtone().removeListener(mOnRingtoneChange);
        mRankingsPollingPreferenceStore.getVibrationEnabled().removeListener(mOnVibrationEnabledChange);
        mRankingsPollingPreferenceStore.getWifiRequired().removeListener(mOnWifiRequiredChange);
    }

    @OnClick(R.id.spvGarTwitter)
    void onGarTwitterClick() {
        mShareUtils.openUrl(this, Constants.GAR_TWITTER_URL);
    }

    @OnClick(R.id.tvGooglePlayServicesError)
    void onGooglePlayServicesErrorClick() {
        final int connectionStatus = mGoogleApiWrapper.getGooglePlayServicesConnectionStatus();

        if (mGoogleApiWrapper.isConnectionStatusSuccess(connectionStatus)) {
            Toast.makeText(this, R.string.google_play_services_error_has_been_resolved,
                    Toast.LENGTH_LONG).show();
            refresh();
            return;
        }

        if (mGoogleApiWrapper.showPlayServicesResolutionDialog(connectionStatus, this,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(final DialogInterface dialog) {
                        refresh();
                    }
                })) {
            return;
        }

        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.google_play_services_error_cant_be_resolved,
                        connectionStatus))
                .setNeutralButton(R.string.ok, null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(final DialogInterface dialog) {
                        refresh();
                    }
                })
                .show();
    }

    @OnClick(R.id.spvLogViewer)
    void onLogViewerClick() {
        startActivity(LogViewerActivity.getLaunchIntent(this));
    }

    @OnClick(R.id.spvGitHub)
    void onGitHubClick() {
        mShareUtils.openUrl(this, Constants.GITHUB_URL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();

        mFavoritePlayersManager.addListener(mOnFavoritePlayersChangeListener);
        mIdentityManager.addListener(mOnIdentityChangeListener);
        mRegionManager.addListener(mOnRegionChangeListener);

        mRankingsPollingPreferenceStore.getChargingRequired().addListener(mOnChargingRequiredChange);
        mRankingsPollingPreferenceStore.getEnabled().addListener(mOnRankingsPollingEnabledChange);
        mRankingsPollingPreferenceStore.getPollFrequency().addListener(mOnPollFrequencyChange);
        mRankingsPollingPreferenceStore.getRingtone().addListener(mOnRingtoneChange);
        mRankingsPollingPreferenceStore.getVibrationEnabled().addListener(mOnVibrationEnabledChange);
        mRankingsPollingPreferenceStore.getWifiRequired().addListener(mOnWifiRequiredChange);

        mUseRankingsPolling.set(mRankingsPollingPreferenceStore.getEnabled());
        mVibrate.set(mRankingsPollingPreferenceStore.getVibrationEnabled());
        mMustBeOnWifi.set(mRankingsPollingPreferenceStore.getWifiRequired());
        mMustBeCharging.set(mRankingsPollingPreferenceStore.getChargingRequired());
    }

    private void refresh() {
        mThemePreferenceView.refresh();
        mIdentityPreferenceView.refresh();
        mDeleteFavoritePlayersPreferenceView.refresh();

        mRegionPreferenceView.refresh();

        mUseRankingsPolling.refresh();
        mPollFrequency.refresh();
        mRingtonePreferenceView.refresh();
        mVibrate.refresh();
        mMustBeOnWifi.refresh();
        mMustBeCharging.refresh();
        mLastPoll.refresh();

        if (mGoogleApiWrapper.isGooglePlayServicesAvailable()) {
            mGooglePlayServicesError.setVisibility(View.GONE);
            mUseRankingsPolling.setEnabled(true);

            if (Boolean.TRUE.equals(mRankingsPollingPreferenceStore.getEnabled().get())) {
                mPollFrequency.setEnabled(true);
                mRingtonePreferenceView.setEnabled(true);
                mVibrate.setEnabled(true);
                mMustBeOnWifi.setEnabled(true);
                mMustBeCharging.setEnabled(true);
            } else {
                mPollFrequency.setEnabled(false);
                mRingtonePreferenceView.setEnabled(false);
                mVibrate.setEnabled(false);
                mMustBeOnWifi.setEnabled(false);
                mMustBeCharging.setEnabled(false);
            }
        } else {
            mGooglePlayServicesError.setVisibility(View.VISIBLE);
            mUseRankingsPolling.setEnabled(false);
            mPollFrequency.setEnabled(false);
            mRingtonePreferenceView.setEnabled(false);
            mVibrate.setEnabled(false);
            mMustBeOnWifi.setEnabled(false);
            mMustBeCharging.setEnabled(false);
        }
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    private final FavoritePlayersManager.OnFavoritePlayersChangeListener mOnFavoritePlayersChangeListener =
            new FavoritePlayersManager.OnFavoritePlayersChangeListener() {
        @Override
        public void onFavoritePlayersChanged(final FavoritePlayersManager manager) {
            refresh();
        }
    };

    private final IdentityManager.OnIdentityChangeListener mOnIdentityChangeListener =
            new IdentityManager.OnIdentityChangeListener() {
        @Override
        public void onIdentityChange(final IdentityManager identityManager) {
            refresh();
        }
    };

    private final Preference.OnPreferenceChangeListener<Boolean> mOnChargingRequiredChange =
            new Preference.OnPreferenceChangeListener<Boolean>() {
        @Override
        public void onPreferenceChange(final Preference<Boolean> preference) {
            mRankingsPollingSyncManager.enableOrDisable();
            refresh();
        }
    };

    private final Preference.OnPreferenceChangeListener<PollFrequency> mOnPollFrequencyChange =
            new Preference.OnPreferenceChangeListener<PollFrequency>() {
        @Override
        public void onPreferenceChange(final Preference<PollFrequency> preference) {
            mRankingsPollingSyncManager.enableOrDisable();
            refresh();
        }
    };

    private final Preference.OnPreferenceChangeListener<Boolean> mOnRankingsPollingEnabledChange =
            new Preference.OnPreferenceChangeListener<Boolean>() {
        @Override
        public void onPreferenceChange(final Preference<Boolean> preference) {
            mRankingsPollingSyncManager.enableOrDisable();
            refresh();
        }
    };

    private final Preference.OnPreferenceChangeListener<Uri> mOnRingtoneChange =
            new Preference.OnPreferenceChangeListener<Uri>() {
        @Override
        public void onPreferenceChange(final Preference<Uri> preference) {
            refresh();
        }
    };

    private final Preference.OnPreferenceChangeListener<Boolean> mOnVibrationEnabledChange =
            new Preference.OnPreferenceChangeListener<Boolean>() {
        @Override
        public void onPreferenceChange(final Preference<Boolean> preference) {
            refresh();
        }
    };

    private final Preference.OnPreferenceChangeListener<Boolean> mOnWifiRequiredChange =
            new Preference.OnPreferenceChangeListener<Boolean>() {
        @Override
        public void onPreferenceChange(final Preference<Boolean> preference) {
            mRankingsPollingSyncManager.enableOrDisable();
            refresh();
        }
    };

    private final RegionManager.OnRegionChangeListener mOnRegionChangeListener =
            new RegionManager.OnRegionChangeListener() {
        @Override
        public void onRegionChange(final RegionManager regionManager) {
            refresh();
        }
    };

}

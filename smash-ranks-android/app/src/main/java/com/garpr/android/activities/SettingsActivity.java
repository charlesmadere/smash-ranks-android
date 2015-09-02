package com.garpr.android.activities;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.garpr.android.App;
import com.garpr.android.BuildConfig;
import com.garpr.android.R;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.NetworkCache;
import com.garpr.android.misc.Notifications;
import com.garpr.android.misc.SyncManager;
import com.garpr.android.misc.Utils;
import com.garpr.android.models.Region;
import com.garpr.android.settings.Settings;
import com.garpr.android.views.BooleanSettingPreferenceView;
import com.garpr.android.views.CheckPreferenceView;
import com.garpr.android.views.PreferenceView;
import com.garpr.android.views.SwitchPreferenceView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import butterknife.Bind;
import butterknife.OnClick;


public class SettingsActivity extends BaseToolbarActivity {


    private static final String TAG = "SettingsActivity";

    @Bind(R.id.activity_settings_sync_charging)
    CheckPreferenceView mSyncCharging;

    @Bind(R.id.activity_settings_sync_wifi)
    CheckPreferenceView mSyncWifi;

    @Bind(R.id.activity_settings_orb)
    ImageButton mOrb;

    @Bind(R.id.activity_settings_author)
    PreferenceView mAuthor;

    @Bind(R.id.activity_settings_console)
    PreferenceView mConsole;

    @Bind(R.id.activity_settings_google_play_services_error)
    PreferenceView mGooglePlayServicesError;

    @Bind(R.id.activity_settings_network_cache)
    PreferenceView mNetworkCache;

    @Bind(R.id.activity_settings_region)
    PreferenceView mRegion;

    @Bind(R.id.activity_settings_sync_status)
    PreferenceView mSyncStatus;

    @Bind(R.id.activity_settings_version)
    PreferenceView mVersion;

    @Bind(R.id.activity_settings_generate_notification)
    TextView mGenerateNotification;

    @Bind(R.id.activity_settings_github)
    TextView mGitHub;

    @Bind(R.id.activity_settings_rate_app)
    TextView mRateApp;

    @Bind(R.id.activity_settings_server)
    TextView mServer;

    @Bind(R.id.activity_settings_sync)
    SwitchPreferenceView mSync;




    public static void start(final Activity activity) {
        final Intent intent = new Intent(activity, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
    }


    @Override
    public String getActivityName() {
        return TAG;
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_settings;
    }


    @Override
    protected int getSelectedNavigationItemId() {
        return R.id.navigation_view_menu_settings;
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareGeneralViews();
        prepareSyncViews();
        prepareCreditsViews();
        prepareMiscellaneousViews();
    }


    @OnClick(R.id.activity_settings_console)
    public void onConsoleClick() {
        ConsoleActivity.start(SettingsActivity.this);
    }


    @OnClick(R.id.activity_settings_github)
    public void onGithubClick() {
        openLink(Constants.GITHUB_URL);
    }


    @OnClick(R.id.activity_settings_google_play_services_error)
    public void onGooglePlayServicesErrorClick() {
        final GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        final Dialog dialog = gaa.getErrorDialog(this,
                Utils.googlePlayServicesConnectionStatus(), 0,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(final DialogInterface dialog) {
                        pollGooglePlayServices();
                    }
                });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface dialog) {
                pollGooglePlayServices();
            }
        });

        dialog.show();
    }


    @OnClick(R.id.activity_settings_network_cache)
    public void onNetworkCacheClick() {
        NetworkCache.clear();
        pollNetworkCache();
    }


    @OnClick(R.id.activity_settings_server)
    public void onServerClick() {
        openLink(Constants.IVAN_TWITTER_URL);
    }


    @OnClick(R.id.activity_settings_author)
    public void onAuthorClick() {
        final String[] items = getResources().getStringArray(R.array.app_authors);

        new AlertDialog.Builder(SettingsActivity.this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                        final String author = items[which];

                        if (author.equalsIgnoreCase(getString(R.string.charles_madere))) {
                            openLink(Constants.CHARLES_TWITTER_URL);
                        } else if (author.equalsIgnoreCase(getString(R.string.timothy_choi))) {
                            // TODO
                            // find out what he wants here...
                        } else {
                            throw new RuntimeException("unknown author: " + author);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    @OnClick(R.id.activity_settings_rate_app)
    public void onRateAppClick() {
        openLink(Constants.GAR_PR_GOOGLE_PLAY_STORE_URL);
    }


    @Override
    public void onRegionChanged(final Region region) {
        super.onRegionChanged(region);
        mRegion.setSubTitleText(region.getName());
    }


    @OnClick(R.id.activity_settings_region)
    public void onRegionClick() {
        RegionsActivity.start(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        pollNetworkCache();
        pollGooglePlayServices();
    }


    private void openLink(final String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }


    private void pollNetworkCache() {
        final int networkCacheSize = NetworkCache.size();
        mNetworkCache.setEnabled(networkCacheSize >= 1);
        mNetworkCache.setSubTitleText(getResources().getQuantityString(
                R.plurals.currently_contains_x_entries, networkCacheSize, networkCacheSize));
    }


    private void pollGooglePlayServices() {
        final int googlePlayServicesConnectionStatus = Utils.googlePlayServicesConnectionStatus();
        final boolean isGooglePlayConnected = googlePlayServicesConnectionStatus == ConnectionResult.SUCCESS;
        mGooglePlayServicesError.setVisibility(isGooglePlayConnected ? View.GONE : View.VISIBLE);
        mSync.setEnabled(isGooglePlayConnected);
        mSyncCharging.setEnabled(isGooglePlayConnected);
        mSyncWifi.setEnabled(isGooglePlayConnected);
    }


    private void prepareCreditsViews() {
        mAuthor.setTitleText(R.string.app_written_by);
        mAuthor.setSubTitleText(R.string.app_authors);
    }


    private void prepareGeneralViews() {
        mRegion.setTitleText(R.string.change_region);
        mRegion.setSubTitleText(Settings.Region.get().getName());

        mNetworkCache.setTitleText(R.string.clear_network_cache);
    }


    private void prepareMiscellaneousViews() {
        mConsole.setTitleText(R.string.log_console);
        mConsole.setSubTitleText(R.string.log_console_description);

        mVersion.setEnabled(false);
        mVersion.setTitleText(R.string.version_information);
        mVersion.setSubTitleText(getString(R.string.x_build_y, App.getVersionName(),
                App.getVersionCode()));
    }


    private void prepareSyncViews() {
        mGooglePlayServicesError.setTitleText(R.string.google_play_services_unavailable);
        mGooglePlayServicesError.setSubTitleText(R.string.period_sync_requires_google_play_services);

        mSync.set(Settings.Sync.IsEnabled, R.string.enable_or_disable_sync,
                R.string.periodic_sync_is_on, R.string.periodic_sync_is_turned_off);
        mSync.setOnToggleListener(new BooleanSettingPreferenceView.OnToggleListener() {
            @Override
            public void onToggle(final BooleanSettingPreferenceView v) {
                final boolean isEnabled = v.getSetting().get();
                mSyncCharging.setEnabled(isEnabled);
                mSyncWifi.setEnabled(isEnabled);

                if (isEnabled) {
                    SyncManager.schedule();
                } else {
                    SyncManager.cancel();
                }
            }
        });

        final boolean isSyncEnabled = mSync.getSetting().get();
        mSyncCharging.setEnabled(isSyncEnabled);
        mSyncWifi.setEnabled(isSyncEnabled);

        mSyncCharging.set(Settings.Sync.ChargingIsNecessary, R.string.only_sync_when_charging,
                R.string.will_only_sync_if_plugged_in,
                R.string.will_sync_regardless_of_being_plugged_in_or_not);

        mSyncWifi.set(Settings.Sync.WifiIsNecessary, R.string.only_sync_on_wifi,
                R.string.will_only_sync_if_connected_to_wifi,
                R.string.will_sync_on_any_data_connection);

        final BooleanSettingPreferenceView.OnToggleListener syncToggleListener =
                new BooleanSettingPreferenceView.OnToggleListener() {
                    @Override
                    public void onToggle(final BooleanSettingPreferenceView v) {
                        SyncManager.schedule();
                    }
                };

        mSyncCharging.setOnToggleListener(syncToggleListener);
        mSyncWifi.setOnToggleListener(syncToggleListener);

        mSyncStatus.setEnabled(false);
        mSyncStatus.setTitleText(R.string.last_sync);

        if (Settings.Sync.LastDate.exists()) {
            final long lastDate = Settings.Sync.LastDate.get();
            mSyncStatus.setSubTitleText(DateUtils.getRelativeDateTimeString(this, lastDate,
                    DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0));
        } else {
            mSyncStatus.setSubTitleText(R.string.sync_has_yet_to_occur);
        }

        if (BuildConfig.DEBUG) {
            mGenerateNotification.setVisibility(View.VISIBLE);
            mGenerateNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Notifications.showRankingsUpdated();
                }
            });
        }
    }


    @OnClick(R.id.activity_settings_orb)
    public void randomYoutubeVideo() {
        int videoIndex;

        do {
            videoIndex = Utils.RANDOM.nextInt(Constants.RANDOM_YOUTUBE_VIDEOS.length);
        } while (videoIndex < 0 || videoIndex >= Constants.RANDOM_YOUTUBE_VIDEOS.length);

        openLink(Constants.RANDOM_YOUTUBE_VIDEOS[videoIndex]);
    }


}

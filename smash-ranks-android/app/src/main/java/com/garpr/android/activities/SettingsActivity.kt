package com.garpr.android.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.misc.*
import com.garpr.android.models.PollFrequency
import com.garpr.android.preferences.Preference
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import com.garpr.android.sync.RankingsPollingSyncManager
import com.garpr.android.views.*
import kotterknife.bindView
import javax.inject.Inject

class SettingsActivity : BaseActivity() {

    @Inject
    lateinit protected var mFavoritePlayersManager: FavoritePlayersManager

    @Inject
    lateinit protected var mGoogleApiWrapper: GoogleApiWrapper

    @Inject
    lateinit protected var mIdentityManager: IdentityManager

    @Inject
    lateinit protected var mRankingsPollingPreferenceStore: RankingsPollingPreferenceStore

    @Inject
    lateinit protected var mRankingsPollingSyncManager: RankingsPollingSyncManager

    @Inject
    lateinit protected var mRegionManager: RegionManager

    @Inject
    lateinit protected var mShareUtils: ShareUtils

    private val mMustBeCharging: CheckablePreferenceView by bindView(R.id.cpvMustBeCharging)
    private val mMustBeOnWifi: CheckablePreferenceView by bindView(R.id.cpvMustBeOnWifi)
    private val mUseRankingsPolling: CheckablePreferenceView by bindView(R.id.cpvUseRankingsPolling)
    private val mVibrate: CheckablePreferenceView by bindView(R.id.cpvVibrate)
    private val mDeleteFavoritePlayersPreferenceView: DeleteFavoritePlayersPreferenceView by bindView(R.id.deleteFavoritePlayersPreferenceView)
    private val mIdentityPreferenceView: IdentityPreferenceView by bindView(R.id.identityPreferenceView)
    private val mLastPoll: LastPollPreferenceView by bindView(R.id.lastPollPreferenceView)
    private val mPollFrequency: PollFrequencyPreferenceView by bindView(R.id.pollFrequencyPreferenceView)
    private val mRegionPreferenceView: RegionPreferenceView by bindView(R.id.regionPreferenceView)
    private val mRingtonePreferenceView: RingtonePreferenceView by bindView(R.id.ringtonePreferenceView)
    private val mCharlesTwitter: SimplePreferenceView by bindView(R.id.spvCharlesTwitter)
    private val mGarTwitter: SimplePreferenceView by bindView(R.id.spvGarTwitter)
    private val mGitHub: SimplePreferenceView by bindView(R.id.spvGitHub)
    private val mLogViewer: SimplePreferenceView by bindView(R.id.spvLogViewer)
    private val mGooglePlayServicesError: TextView by bindView(R.id.tvGooglePlayServicesError)
    private val mThemePreferenceView: ThemePreferenceView by bindView(R.id.themePreferenceView)


    companion object {
        private const val TAG = "SettingsActivity"

        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }

    override val activityName = TAG

    private fun attemptToResolveGooglePlayServicesError() {
        val connectionStatus = mGoogleApiWrapper.googlePlayServicesConnectionStatus

        if (mGoogleApiWrapper.isConnectionStatusSuccess(connectionStatus)) {
            Toast.makeText(this, R.string.google_play_services_error_has_been_resolved,
                    Toast.LENGTH_LONG).show()
            refresh()
            return
        }

        if (mGoogleApiWrapper.showPlayServicesResolutionDialog(connectionStatus, this,
                DialogInterface.OnCancelListener { refresh() })) {
            return
        }

        AlertDialog.Builder(this)
                .setMessage(getString(R.string.google_play_services_error_cant_be_resolved,
                        connectionStatus))
                .setNeutralButton(R.string.ok, null)
                .setOnDismissListener { refresh() }
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ResultCodes.RINGTONE_SELECTED.mValue) {
            mRingtonePreferenceView.onActivityResult(data)
        }

        refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_settings)
    }

    override fun onDestroy() {
        super.onDestroy()

        mFavoritePlayersManager.removeListener(mOnFavoritePlayersChangeListener)
        mIdentityManager.removeListener(mOnIdentityChangeListener)
        mRegionManager.removeListener(mOnRegionChangeListener)

        mRankingsPollingPreferenceStore.chargingRequired.removeListener(mOnChargingRequiredChange)
        mRankingsPollingPreferenceStore.enabled.removeListener(mOnRankingsPollingEnabledChange)
        mRankingsPollingPreferenceStore.pollFrequency.removeListener(mOnPollFrequencyChange)
        mRankingsPollingPreferenceStore.ringtone.removeListener(mOnRingtoneChange)
        mRankingsPollingPreferenceStore.vibrationEnabled.removeListener(mOnVibrationEnabledChange)
        mRankingsPollingPreferenceStore.wifiRequired.removeListener(mOnWifiRequiredChange)
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        mFavoritePlayersManager.addListener(mOnFavoritePlayersChangeListener)
        mIdentityManager.addListener(mOnIdentityChangeListener)
        mRegionManager.addListener(mOnRegionChangeListener)

        mRankingsPollingPreferenceStore.chargingRequired.addListener(mOnChargingRequiredChange)
        mRankingsPollingPreferenceStore.enabled.addListener(mOnRankingsPollingEnabledChange)
        mRankingsPollingPreferenceStore.pollFrequency.addListener(mOnPollFrequencyChange)
        mRankingsPollingPreferenceStore.ringtone.addListener(mOnRingtoneChange)
        mRankingsPollingPreferenceStore.vibrationEnabled.addListener(mOnVibrationEnabledChange)
        mRankingsPollingPreferenceStore.wifiRequired.addListener(mOnWifiRequiredChange)

        mUseRankingsPolling.set(mRankingsPollingPreferenceStore.enabled)
        mVibrate.set(mRankingsPollingPreferenceStore.vibrationEnabled)
        mMustBeOnWifi.set(mRankingsPollingPreferenceStore.wifiRequired)
        mMustBeCharging.set(mRankingsPollingPreferenceStore.chargingRequired)

        mGooglePlayServicesError.setOnClickListener { attemptToResolveGooglePlayServicesError() }

        mCharlesTwitter.setOnClickListener {
            mShareUtils.openUrl(this, Constants.CHARLES_TWITTER_URL)
        }

        mGarTwitter.setOnClickListener {
            mShareUtils.openUrl(this, Constants.GAR_TWITTER_URL)
        }

        mGitHub.setOnClickListener {
            mShareUtils.openUrl(this, Constants.GITHUB_URL)
        }

        mLogViewer.setOnClickListener {
            startActivity(LogViewerActivity.getLaunchIntent(this))
        }
    }

    private fun refresh() {
        mThemePreferenceView.refresh()
        mIdentityPreferenceView.refresh()
        mDeleteFavoritePlayersPreferenceView.refresh()

        mRegionPreferenceView.refresh()

        mUseRankingsPolling.refresh()
        mPollFrequency.refresh()
        mRingtonePreferenceView.refresh()
        mVibrate.refresh()
        mMustBeOnWifi.refresh()
        mMustBeCharging.refresh()
        mLastPoll.refresh()

        if (mGoogleApiWrapper.isGooglePlayServicesAvailable) {
            mGooglePlayServicesError.visibility = View.GONE
            mUseRankingsPolling.isEnabled = true

            if (mRankingsPollingPreferenceStore.enabled.get() == true) {
                mPollFrequency.isEnabled = true
                mRingtonePreferenceView.isEnabled = true
                mVibrate.isEnabled = true
                mMustBeOnWifi.isEnabled = true
                mMustBeCharging.isEnabled = true
            } else {
                mPollFrequency.isEnabled = false
                mRingtonePreferenceView.isEnabled = false
                mVibrate.isEnabled = false
                mMustBeOnWifi.isEnabled = false
                mMustBeCharging.isEnabled = false
            }
        } else {
            mGooglePlayServicesError.visibility = View.VISIBLE
            mUseRankingsPolling.isEnabled = false
            mPollFrequency.isEnabled = false
            mRingtonePreferenceView.isEnabled = false
            mVibrate.isEnabled = false
            mMustBeOnWifi.isEnabled = false
            mMustBeCharging.isEnabled = false
        }
    }

    override val showUpNavigation = true

    private val mOnFavoritePlayersChangeListener = object : FavoritePlayersManager.OnFavoritePlayersChangeListener {
        override fun onFavoritePlayersChanged(manager: FavoritePlayersManager) {
            refresh()
        }
    }

    private val mOnIdentityChangeListener = object : IdentityManager.OnIdentityChangeListener {
        override fun onIdentityChange(identityManager: IdentityManager) {
            refresh()
        }
    }

    private val mOnChargingRequiredChange = Preference.OnPreferenceChangeListener<Boolean> {
        mRankingsPollingSyncManager.enableOrDisable()
        refresh()
    }

    private val mOnPollFrequencyChange = Preference.OnPreferenceChangeListener<PollFrequency> {
        mRankingsPollingSyncManager.enableOrDisable()
        refresh()
    }

    private val mOnRankingsPollingEnabledChange = Preference.OnPreferenceChangeListener<Boolean> {
        mRankingsPollingSyncManager.enableOrDisable()
        refresh()
    }

    private val mOnRingtoneChange = Preference.OnPreferenceChangeListener<Uri> {
        refresh()
    }

    private val mOnVibrationEnabledChange = Preference.OnPreferenceChangeListener<Boolean> {
        refresh()
    }

    private val mOnWifiRequiredChange = Preference.OnPreferenceChangeListener<Boolean> {
        mRankingsPollingSyncManager.enableOrDisable()
        refresh()
    }

    private val mOnRegionChangeListener = object : RegionManager.OnRegionChangeListener {
        override fun onRegionChange(regionManager: RegionManager) {
            refresh()
        }
    }

}

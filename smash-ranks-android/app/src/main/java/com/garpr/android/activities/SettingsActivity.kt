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
import butterknife.OnClick
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.misc.Constants
import com.garpr.android.misc.FavoritePlayersManager
import com.garpr.android.misc.GoogleApiWrapper
import com.garpr.android.misc.IdentityManager
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.ResultCodes
import com.garpr.android.misc.ShareUtils
import com.garpr.android.models.PollFrequency
import com.garpr.android.preferences.Preference
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import com.garpr.android.sync.RankingsPollingSyncManager
import com.garpr.android.views.CheckablePreferenceView
import com.garpr.android.views.DeleteFavoritePlayersPreferenceView
import com.garpr.android.views.IdentityPreferenceView
import com.garpr.android.views.LastPollPreferenceView
import com.garpr.android.views.PollFrequencyPreferenceView
import com.garpr.android.views.RegionPreferenceView
import com.garpr.android.views.RingtonePreferenceView
import com.garpr.android.views.ThemePreferenceView
import kotterknife.bindView
import javax.inject.Inject

class SettingsActivity : BaseKotlinActivity() {

    @Inject
    lateinit internal var mFavoritePlayersManager: FavoritePlayersManager

    @Inject
    lateinit internal var mGoogleApiWrapper: GoogleApiWrapper

    @Inject
    lateinit internal var mIdentityManager: IdentityManager

    @Inject
    lateinit internal var mRankingsPollingPreferenceStore: RankingsPollingPreferenceStore

    @Inject
    lateinit internal var mRankingsPollingSyncManager: RankingsPollingSyncManager

    @Inject
    lateinit internal var mRegionManager: RegionManager

    @Inject
    lateinit internal var mShareUtils: ShareUtils

    internal val mMustBeCharging: CheckablePreferenceView by bindView(R.id.cpvMustBeCharging)
    internal val mMustBeOnWifi: CheckablePreferenceView by bindView(R.id.cpvMustBeOnWifi)
    internal val mUseRankingsPolling: CheckablePreferenceView by bindView(R.id.cpvUseRankingsPolling)
    internal val mVibrate: CheckablePreferenceView by bindView(R.id.cpvVibrate)
    internal val mDeleteFavoritePlayersPreferenceView: DeleteFavoritePlayersPreferenceView by bindView(R.id.deleteFavoritePlayersPreferenceView)
    internal val mIdentityPreferenceView: IdentityPreferenceView by bindView(R.id.identityPreferenceView)
    internal val mLastPoll: LastPollPreferenceView by bindView(R.id.lastPollPreferenceView)
    internal val mPollFrequency: PollFrequencyPreferenceView by bindView(R.id.pollFrequencyPreferenceView)
    internal val mRegionPreferenceView: RegionPreferenceView by bindView(R.id.regionPreferenceView)
    internal val mRingtonePreferenceView: RingtonePreferenceView by bindView(R.id.ringtonePreferenceView)
    internal val mGooglePlayServicesError: TextView by bindView(R.id.tvGooglePlayServicesError)
    internal val mThemePreferenceView: ThemePreferenceView by bindView(R.id.themePreferenceView)


    companion object {
        private val TAG = "SettingsActivity"

        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }

    public override fun getActivityName(): String {
        return TAG
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ResultCodes.RINGTONE_SELECTED.mValue) {
            mRingtonePreferenceView.onActivityResult(data)
        }

        refresh()
    }

    @OnClick(R.id.spvCharlesTwitter)
    internal fun onCharlesTwitterClick() {
        mShareUtils.openUrl(this, Constants.CHARLES_TWITTER_URL)
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

    @OnClick(R.id.spvGarTwitter)
    internal fun onGarTwitterClick() {
        mShareUtils.openUrl(this, Constants.GAR_TWITTER_URL)
    }

    @OnClick(R.id.tvGooglePlayServicesError)
    internal fun onGooglePlayServicesErrorClick() {
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

    @OnClick(R.id.spvLogViewer)
    internal fun onLogViewerClick() {
        startActivity(LogViewerActivity.getLaunchIntent(this))
    }

    @OnClick(R.id.spvGitHub)
    internal fun onGitHubClick() {
        mShareUtils.openUrl(this, Constants.GITHUB_URL)
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

    override fun showUpNavigation(): Boolean {
        return true
    }

    private val mOnFavoritePlayersChangeListener = FavoritePlayersManager.OnFavoritePlayersChangeListener {
        refresh()
    }

    private val mOnIdentityChangeListener = IdentityManager.OnIdentityChangeListener {
        refresh()
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

    private val mOnRegionChangeListener = RegionManager.OnRegionChangeListener {
        refresh()
    }

}
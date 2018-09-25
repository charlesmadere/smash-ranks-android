package com.garpr.android.activities

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.FavoritePlayersManager
import com.garpr.android.managers.IdentityManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.Constants
import com.garpr.android.misc.RequestCodes
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
import com.garpr.android.views.SimplePreferenceView
import com.garpr.android.views.SmashRosterSyncPreferenceView
import com.garpr.android.views.ThemePreferenceView
import com.garpr.android.wrappers.GoogleApiWrapper
import kotterknife.bindView
import javax.inject.Inject

class SettingsActivity : BaseActivity() {

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var googleApiWrapper: GoogleApiWrapper

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var rankingsPollingPreferenceStore: RankingsPollingPreferenceStore

    @Inject
    protected lateinit var rankingsPollingSyncManager: RankingsPollingSyncManager

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var shareUtils: ShareUtils

    private val mustBeCharging: CheckablePreferenceView by bindView(R.id.cpvMustBeCharging)
    private val mustBeOnWifi: CheckablePreferenceView by bindView(R.id.cpvMustBeOnWifi)
    private val useRankingsPolling: CheckablePreferenceView by bindView(R.id.cpvUseRankingsPolling)
    private val vibrate: CheckablePreferenceView by bindView(R.id.cpvVibrate)
    private val deleteFavoritePlayersPreferenceView: DeleteFavoritePlayersPreferenceView by bindView(R.id.deleteFavoritePlayersPreferenceView)
    private val identityPreferenceView: IdentityPreferenceView by bindView(R.id.identityPreferenceView)
    private val lastPoll: LastPollPreferenceView by bindView(R.id.lastPollPreferenceView)
    private val pollFrequency: PollFrequencyPreferenceView by bindView(R.id.pollFrequencyPreferenceView)
    private val regionPreferenceView: RegionPreferenceView by bindView(R.id.regionPreferenceView)
    private val ringtonePreferenceView: RingtonePreferenceView by bindView(R.id.ringtonePreferenceView)
    private val charlesTwitter: SimplePreferenceView by bindView(R.id.spvCharlesTwitter)
    private val garTwitter: SimplePreferenceView by bindView(R.id.spvGarTwitter)
    private val gitHub: SimplePreferenceView by bindView(R.id.spvGitHub)
    private val logViewer: SimplePreferenceView by bindView(R.id.spvLogViewer)
    private val tsuaiiTwitter: SimplePreferenceView by bindView(R.id.spvTsuaiiTwitter)
    private val smashRosterFormLink: SimplePreferenceView by bindView(R.id.smashRosterFormLink)
    private val smashRosterPreferenceView: SmashRosterSyncPreferenceView by bindView(R.id.smashRosterPreferenceView)
    private val rankingsPollingGooglePlayServicesError: TextView by bindView(R.id.tvRankingsPollingGooglePlayServicesError)
    private val smashRosterGooglePlayServicesError: TextView by bindView(R.id.tvSmashRosterGooglePlayServicesError)
    private val themePreferenceView: ThemePreferenceView by bindView(R.id.themePreferenceView)


    companion object {
        private const val TAG = "SettingsActivity"

        fun getLaunchIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }

    override val activityName = TAG

    private fun attemptToResolveGooglePlayServicesError() {
        val connectionStatus = googleApiWrapper.googlePlayServicesConnectionStatus

        if (googleApiWrapper.isConnectionStatusSuccess(connectionStatus)) {
            Toast.makeText(this, R.string.google_play_services_error_has_been_resolved,
                    Toast.LENGTH_LONG).show()
            refresh()
            return
        }

        if (googleApiWrapper.showPlayServicesResolutionDialog(this, connectionStatus,
                googleApiWrapperOnCancelListener)) {
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

        when (requestCode) {
            RequestCodes.CHANGE_IDENTITY.value -> {
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, R.string.identity_saved_, Toast.LENGTH_LONG).show()
                }
            }

            RequestCodes.CHANGE_REGION.value -> {
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, R.string.region_saved_, Toast.LENGTH_LONG).show()
                }
            }

            RequestCodes.CHANGE_RINGTONE.value -> {
                ringtonePreferenceView.onActivityResult(data)
            }
        }

        refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(R.layout.activity_settings)
    }

    override fun onDestroy() {
        super.onDestroy()

        favoritePlayersManager.removeListener(onFavoritePlayersChangeListener)
        identityManager.removeListener(onIdentityChangeListener)
        regionManager.removeListener(onRegionChangeListener)

        rankingsPollingPreferenceStore.chargingRequired.removeListener(onChargingRequiredChange)
        rankingsPollingPreferenceStore.enabled.removeListener(onRankingsPollingEnabledChange)
        rankingsPollingPreferenceStore.pollFrequency.removeListener(onPollFrequencyChange)
        rankingsPollingPreferenceStore.ringtone.removeListener(onRingtoneChange)
        rankingsPollingPreferenceStore.vibrationEnabled.removeListener(onVibrationEnabledChange)
        rankingsPollingPreferenceStore.wifiRequired.removeListener(onWifiRequiredChange)
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        favoritePlayersManager.addListener(onFavoritePlayersChangeListener)
        identityManager.addListener(onIdentityChangeListener)
        regionManager.addListener(onRegionChangeListener)

        rankingsPollingPreferenceStore.chargingRequired.addListener(onChargingRequiredChange)
        rankingsPollingPreferenceStore.enabled.addListener(onRankingsPollingEnabledChange)
        rankingsPollingPreferenceStore.pollFrequency.addListener(onPollFrequencyChange)
        rankingsPollingPreferenceStore.ringtone.addListener(onRingtoneChange)
        rankingsPollingPreferenceStore.vibrationEnabled.addListener(onVibrationEnabledChange)
        rankingsPollingPreferenceStore.wifiRequired.addListener(onWifiRequiredChange)

        useRankingsPolling.preference = rankingsPollingPreferenceStore.enabled
        vibrate.preference = rankingsPollingPreferenceStore.vibrationEnabled
        mustBeOnWifi.preference = rankingsPollingPreferenceStore.wifiRequired
        mustBeCharging.preference = rankingsPollingPreferenceStore.chargingRequired

        rankingsPollingGooglePlayServicesError.setOnClickListener {
            attemptToResolveGooglePlayServicesError()
        }

        smashRosterGooglePlayServicesError.setOnClickListener {
            attemptToResolveGooglePlayServicesError()
        }

        smashRosterFormLink.setOnClickListener {
            shareUtils.openUrl(this, Constants.SMASH_ROSTER_FORM_URL)
        }

        charlesTwitter.setOnClickListener {
            shareUtils.openUrl(this, Constants.CHARLES_TWITTER_URL)
        }

        gitHub.setOnClickListener {
            shareUtils.openUrl(this, Constants.GITHUB_URL)
        }

        garTwitter.setOnClickListener {
            shareUtils.openUrl(this, Constants.GAR_TWITTER_URL)
        }

        tsuaiiTwitter.setOnClickListener {
            shareUtils.openUrl(this, Constants.TSUAII_TWITTER_URL)
        }

        logViewer.setOnClickListener {
            startActivity(LogViewerActivity.getLaunchIntent(this))
        }
    }

    private fun refresh() {
        regionPreferenceView.refresh()
        themePreferenceView.refresh()
        identityPreferenceView.refresh()
        deleteFavoritePlayersPreferenceView.refresh()

        useRankingsPolling.refresh()
        pollFrequency.refresh()
        ringtonePreferenceView.refresh()
        vibrate.refresh()
        mustBeOnWifi.refresh()
        mustBeCharging.refresh()
        lastPoll.refresh()

        smashRosterPreferenceView.refresh()

        if (googleApiWrapper.isGooglePlayServicesAvailable) {
            rankingsPollingGooglePlayServicesError.visibility = View.GONE
            smashRosterGooglePlayServicesError.visibility = View.GONE
            useRankingsPolling.isEnabled = true

            if (rankingsPollingPreferenceStore.enabled.get() == true) {
                pollFrequency.isEnabled = true
                ringtonePreferenceView.isEnabled = true
                vibrate.isEnabled = true
                mustBeOnWifi.isEnabled = true
                mustBeCharging.isEnabled = true
            } else {
                pollFrequency.isEnabled = false
                ringtonePreferenceView.isEnabled = false
                vibrate.isEnabled = false
                mustBeOnWifi.isEnabled = false
                mustBeCharging.isEnabled = false
            }
        } else {
            rankingsPollingGooglePlayServicesError.visibility = View.VISIBLE
            smashRosterGooglePlayServicesError.visibility = View.VISIBLE
            useRankingsPolling.isEnabled = false
            pollFrequency.isEnabled = false
            ringtonePreferenceView.isEnabled = false
            vibrate.isEnabled = false
            mustBeOnWifi.isEnabled = false
            mustBeCharging.isEnabled = false
        }
    }

    override val showUpNavigation = true

    private val googleApiWrapperOnCancelListener = DialogInterface.OnCancelListener { refresh() }

    private val onFavoritePlayersChangeListener = object : FavoritePlayersManager.OnFavoritePlayersChangeListener {
        override fun onFavoritePlayersChange(favoritePlayersManager: FavoritePlayersManager) {
            refresh()
        }
    }

    private val onIdentityChangeListener = object : IdentityManager.OnIdentityChangeListener {
        override fun onIdentityChange(identityManager: IdentityManager) {
            refresh()
        }
    }

    private val onChargingRequiredChange = object : Preference.OnPreferenceChangeListener<Boolean> {
        override fun onPreferenceChange(preference: Preference<Boolean>) {
            rankingsPollingSyncManager.enableOrDisable()
            refresh()
        }
    }

    private val onPollFrequencyChange = object : Preference.OnPreferenceChangeListener<PollFrequency> {
        override fun onPreferenceChange(preference: Preference<PollFrequency>) {
            rankingsPollingSyncManager.enableOrDisable()
            refresh()
        }
    }

    private val onRankingsPollingEnabledChange = object : Preference.OnPreferenceChangeListener<Boolean> {
        override fun onPreferenceChange(preference: Preference<Boolean>) {
            rankingsPollingSyncManager.enableOrDisable()
            refresh()
        }
    }

    private val onRingtoneChange = object : Preference.OnPreferenceChangeListener<Uri> {
        override fun onPreferenceChange(preference: Preference<Uri>) {
            refresh()
        }
    }

    private val onVibrationEnabledChange = object : Preference.OnPreferenceChangeListener<Boolean> {
        override fun onPreferenceChange(preference: Preference<Boolean>) {
            refresh()
        }
    }

    private val onWifiRequiredChange = object : Preference.OnPreferenceChangeListener<Boolean> {
        override fun onPreferenceChange(preference: Preference<Boolean>) {
            rankingsPollingSyncManager.enableOrDisable()
            refresh()
        }
    }

    private val onRegionChangeListener = object : RegionManager.OnRegionChangeListener {
        override fun onRegionChange(regionManager: RegionManager) {
            refresh()
        }
    }

}

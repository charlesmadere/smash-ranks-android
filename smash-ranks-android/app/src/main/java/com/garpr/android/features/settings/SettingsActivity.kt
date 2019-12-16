package com.garpr.android.features.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.garpr.android.R
import com.garpr.android.data.models.NightMode
import com.garpr.android.data.models.PollFrequency
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.features.home.HomeActivity
import com.garpr.android.features.logViewer.LogViewerActivity
import com.garpr.android.features.setIdentity.SetIdentityActivity
import com.garpr.android.features.setRegion.SetRegionActivity
import com.garpr.android.features.settings.SettingsViewModel.FavoritePlayersState
import com.garpr.android.features.settings.SettingsViewModel.IdentityState
import com.garpr.android.features.settings.SettingsViewModel.RankingsPollingState
import com.garpr.android.features.settings.SettingsViewModel.SmashRosterState
import com.garpr.android.misc.Constants
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.RequestCodes
import com.garpr.android.misc.ShareUtils
import com.garpr.android.preferences.Preference
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import com.garpr.android.sync.rankings.RankingsPollingManager
import kotlinx.android.synthetic.main.activity_settings.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : BaseActivity(), DeleteFavoritePlayersPreferenceView.Listener,
        IdentityPreferenceView.Listeners, ThemePreferenceView.Listener, Refreshable,
        RegionPreferenceView.Listener, SmashRosterSyncPreferenceView.Listener {

    private val viewModel: SettingsViewModel by viewModel()

    protected val rankingsPollingManager: RankingsPollingManager by inject()
    protected val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore by inject()
    protected val shareUtils: ShareUtils by inject()

    companion object {
        private const val TAG = "SettingsActivity"

        fun getLaunchIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }

    override val activityName = TAG

    private fun initListeners() {
        viewModel.rankingsPollingStateLiveData.observe(this, Observer {
            refreshRankingsPollingState(it)
        })

        viewModel.stateLiveData.observe(this, Observer {
            refreshState(it)
        })

        rankingsPollingPreferenceStore.chargingRequired.addListener(onChargingRequiredChange)
        rankingsPollingPreferenceStore.enabled.addListener(onRankingsPollingEnabledChange)
        rankingsPollingPreferenceStore.pollFrequency.addListener(onPollFrequencyChange)
        rankingsPollingPreferenceStore.ringtone.addListener(onRingtoneChange)
        rankingsPollingPreferenceStore.vibrationEnabled.addListener(onVibrationEnabledChange)
        rankingsPollingPreferenceStore.wifiRequired.addListener(onWifiRequiredChange)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RequestCodes.CHANGE_IDENTITY.value -> {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, R.string.identity_saved_, Toast.LENGTH_LONG).show()
                }
            }

            RequestCodes.CHANGE_REGION.value -> {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, R.string.region_saved_, Toast.LENGTH_LONG).show()
                }
            }

            RequestCodes.CHANGE_RINGTONE.value -> {
                ringtonePreference.onActivityResult(data)
            }
        }

        refresh()
    }

    override fun onClick(v: RegionPreferenceView) {
        startActivityForResult(SetRegionActivity.getLaunchIntent(this),
                RequestCodes.CHANGE_REGION.value)
    }

    override fun onClick(v: SmashRosterSyncPreferenceView) {
        viewModel.syncSmashRoster()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initListeners()
    }

    override fun onDeleteFavoritePlayersClick(v: DeleteFavoritePlayersPreferenceView) {
        viewModel.deleteFavoritePlayers()
    }

    override fun onDeleteIdentityClick() {
        viewModel.deleteIdentity()
    }

    override fun onDestroy() {
        super.onDestroy()

        rankingsPollingPreferenceStore.chargingRequired.removeListener(onChargingRequiredChange)
        rankingsPollingPreferenceStore.enabled.removeListener(onRankingsPollingEnabledChange)
        rankingsPollingPreferenceStore.pollFrequency.removeListener(onPollFrequencyChange)
        rankingsPollingPreferenceStore.ringtone.removeListener(onRingtoneChange)
        rankingsPollingPreferenceStore.vibrationEnabled.removeListener(onVibrationEnabledChange)
        rankingsPollingPreferenceStore.wifiRequired.removeListener(onWifiRequiredChange)
    }

    override fun onNightModeChange(v: ThemePreferenceView, nightMode: NightMode) {
        viewModel.setNightMode(nightMode)

        startActivity(HomeActivity.getLaunchIntent(
                context = this,
                restartActivityTask = true
        ))
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onSetIdentityClick() {
        startActivityForResult(SetIdentityActivity.getLaunchIntent(this),
                RequestCodes.CHANGE_IDENTITY.value)
    }

    override fun onViewsBound() {
        super.onViewsBound()

        regionPreference.listener = this
        identityPreference.listeners = this
        themePreference.listener = this
        deleteFavoritePlayersPreference.listener = this

        useRankingsPolling.preference = rankingsPollingPreferenceStore.enabled
        vibratePreference.preference = rankingsPollingPreferenceStore.vibrationEnabled
        mustBeOnWifiPreference.preference = rankingsPollingPreferenceStore.wifiRequired
        mustBeChargingPreference.preference = rankingsPollingPreferenceStore.chargingRequired

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

        germWebsite.setOnClickListener {
            shareUtils.openUrl(this, Constants.GERM_WEBSITE_URL)
        }

        logViewer.setOnClickListener {
            startActivity(LogViewerActivity.getLaunchIntent(this))
        }
    }

    override fun refresh() {
        useRankingsPolling.refresh()
        rankingsPollingFrequencyPreference.refresh()
        ringtonePreference.refresh()
        vibratePreference.refresh()
        mustBeOnWifiPreference.refresh()
        mustBeChargingPreference.refresh()
        lastPollPreference.refresh()

        if (rankingsPollingManager.isEnabled) {
            vibratePreference.isEnabled = true
            mustBeOnWifiPreference.isEnabled = true
            mustBeChargingPreference.isEnabled = true
        } else {
            vibratePreference.isEnabled = false
            mustBeOnWifiPreference.isEnabled = false
            mustBeChargingPreference.isEnabled = false
        }

        smashRosterPreference.refresh()
    }

    private fun refreshRankingsPollingState(state: RankingsPollingState) {
        // TODO
    }

    private fun refreshState(state: SettingsViewModel.State) {
        regionPreference.setContent(state.region)

        when (state.identityState) {
            is IdentityState.Fetched -> identityPreference.setContent(state.identityState.identity)
            is IdentityState.Fetching -> identityPreference.setLoading()
        }

        themePreference.setContent(state.nightMode)

        when (state.favoritePlayersState) {
            is FavoritePlayersState.Fetched -> {
                deleteFavoritePlayersPreference.setContent(state.favoritePlayersState.size)
            }

            is FavoritePlayersState.Fetching -> deleteFavoritePlayersPreference.setLoading()
        }

        when (state.smashRosterState) {
            is SmashRosterState.Fetched -> {
                smashRosterPreference.setContent(state.smashRosterState.result)
            }

            is SmashRosterState.Fetching -> smashRosterPreference.setLoading()
            is SmashRosterState.IsSyncing -> smashRosterPreference.setSyncing()
        }
    }

    private val onChargingRequiredChange = object : Preference.OnPreferenceChangeListener<Boolean> {
        override fun onPreferenceChange(preference: Preference<Boolean>) {
            rankingsPollingManager.enableOrDisable()
            refresh()
        }
    }

    private val onPollFrequencyChange = object : Preference.OnPreferenceChangeListener<PollFrequency> {
        override fun onPreferenceChange(preference: Preference<PollFrequency>) {
            rankingsPollingManager.enableOrDisable()
            refresh()
        }
    }

    private val onRankingsPollingEnabledChange = object : Preference.OnPreferenceChangeListener<Boolean> {
        override fun onPreferenceChange(preference: Preference<Boolean>) {
            rankingsPollingManager.enableOrDisable()
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
            rankingsPollingManager.enableOrDisable()
            refresh()
        }
    }

}

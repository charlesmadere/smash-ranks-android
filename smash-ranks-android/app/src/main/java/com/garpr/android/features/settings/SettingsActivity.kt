package com.garpr.android.features.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.garpr.android.R
import com.garpr.android.data.models.PollFrequency
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.features.logViewer.LogViewerActivity
import com.garpr.android.misc.Constants
import com.garpr.android.misc.RequestCodes
import com.garpr.android.misc.ShareUtils
import com.garpr.android.preferences.Preference
import com.garpr.android.preferences.RankingsPollingPreferenceStore
import com.garpr.android.repositories.FavoritePlayersRepository
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.RegionRepository
import com.garpr.android.sync.rankings.RankingsPollingManager
import kotlinx.android.synthetic.main.activity_settings.*
import org.koin.android.ext.android.inject

class SettingsActivity : BaseActivity() {

    protected val favoritePlayersRepository: FavoritePlayersRepository by inject()
    protected val identityRepository: IdentityRepository by inject()
    protected val rankingsPollingManager: RankingsPollingManager by inject()
    protected val rankingsPollingPreferenceStore: RankingsPollingPreferenceStore by inject()
    protected val regionRepository: RegionRepository by inject()
    protected val shareUtils: ShareUtils by inject()

    companion object {
        private const val TAG = "SettingsActivity"

        fun getLaunchIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }

    override val activityName = TAG

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
                ringtonePreference.onActivityResult(data)
            }
        }

        refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun onDestroy() {
        super.onDestroy()

        favoritePlayersRepository.removeListener(onFavoritePlayersChangeListener)
        identityRepository.removeListener(onIdentityChangeListener)
        regionRepository.removeListener(onRegionChangeListener)

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

        favoritePlayersRepository.addListener(onFavoritePlayersChangeListener)
        identityRepository.addListener(onIdentityChangeListener)
        regionRepository.addListener(onRegionChangeListener)

        rankingsPollingPreferenceStore.chargingRequired.addListener(onChargingRequiredChange)
        rankingsPollingPreferenceStore.enabled.addListener(onRankingsPollingEnabledChange)
        rankingsPollingPreferenceStore.pollFrequency.addListener(onPollFrequencyChange)
        rankingsPollingPreferenceStore.ringtone.addListener(onRingtoneChange)
        rankingsPollingPreferenceStore.vibrationEnabled.addListener(onVibrationEnabledChange)
        rankingsPollingPreferenceStore.wifiRequired.addListener(onWifiRequiredChange)

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

    private fun refresh() {
        regionPreference.refresh()
        themePreference.refresh()
        identityPreference.refresh()
        deleteFavoritePlayersPreference.refresh()

        useRankingsPolling.refresh()
        rankingsPollingFrequencyPreference.refresh()
        ringtonePreference.refresh()
        vibratePreference.refresh()
        mustBeOnWifiPreference.refresh()
        mustBeChargingPreference.refresh()
        lastPollPreference.refresh()

        smashRosterPreference.refresh()
    }

    private val onFavoritePlayersChangeListener = object : FavoritePlayersRepository.OnFavoritePlayersChangeListener {
        override fun onFavoritePlayersChange(favoritePlayersRepository: FavoritePlayersRepository) {
            refresh()
        }
    }

    private val onIdentityChangeListener = object : IdentityRepository.OnIdentityChangeListener {
        override fun onIdentityChange(identityRepository: IdentityRepository) {
            refresh()
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

    private val onRegionChangeListener = object : RegionRepository.OnRegionChangeListener {
        override fun onRegionChange(regionRepository: RegionRepository) {
            refresh()
        }
    }

}

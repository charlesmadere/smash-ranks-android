package com.garpr.android.features.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.view.HapticFeedbackConstants
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
import com.garpr.android.misc.RequestCodes
import com.garpr.android.misc.ShareUtils
import kotlinx.android.synthetic.main.activity_settings.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.net.Uri as AndroidUri

class SettingsActivity : BaseActivity() {

    private val viewModel: SettingsViewModel by viewModel()

    protected val shareUtils: ShareUtils by inject()

    companion object {
        private const val TAG = "SettingsActivity"

        fun getLaunchIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }

    override val activityName = TAG

    private fun initListeners() {
        viewModel.favoritePlayersStateLiveData.observe(this, Observer { state ->
            refreshFavoritePlayersState(state)
        })

        viewModel.identityStateLiveData.observe(this, Observer { state ->
            refreshIdentityState(state)
        })

        viewModel.nightModeLiveData.observe(this, Observer { nightMode ->
            nightModePreference.setContent(nightMode)
        })

        viewModel.rankingsPollingStateLiveData.observe(this, Observer { state ->
            refreshRankingsPollingState(state)
        })

        viewModel.regionLiveData.observe(this, Observer { region ->
            regionPreference.setContent(region)
        })

        viewModel.smashRosterStateLiveData.observe(this, Observer { state ->
            refreshSmashRosterState(state)
        })
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
                if (resultCode == RESULT_OK) {
                    saveSmashRosterRingtone(data)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initListeners()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        regionPreference.listener = regionPreferenceListener
        identityPreference.listeners = identityPreferenceListeners
        nightModePreference.listener = nightModePreferenceListener
        deleteFavoritePlayersPreference.listener = deleteFavoritePlayersListener
        rankingsPollingPollFrequencyPreference.listener = rankingsPollingPollFrequencyListener
        rankingsPollingRingtonePreference.listener = ringtonePreferenceListener
        rankingsPollingEnabledPreference.listener = rankingsPollingEnabledListener
        rankingsPollingChargingPreference.listener = rankingsPollingChargingListener
        rankingsPollingVibratePreference.listener = rankingsPollingVibrateListener
        rankingsPollingWifiPreference.listener = rankingsPollingWifiListener
        smashRosterPreference.listener = smashRosterPreferenceListener

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

    private fun refreshFavoritePlayersState(state: FavoritePlayersState) {
        when (state) {
            is FavoritePlayersState.Fetched -> {
                deleteFavoritePlayersPreference.setContent(state.size)
            }

            is FavoritePlayersState.Fetching -> deleteFavoritePlayersPreference.setLoading()
        }
    }

    private fun refreshIdentityState(state: IdentityState) {
        when (state) {
            is IdentityState.Fetched -> identityPreference.setContent(state.identity)
            is IdentityState.Fetching -> identityPreference.setLoading()
        }
    }

    private fun refreshRankingsPollingState(state: RankingsPollingState) {
        rankingsPollingEnabledPreference.isChecked = state.isEnabled
        rankingsPollingPollFrequencyPreference.setContent(state.pollFrequency)
        rankingsPollingPollFrequencyPreference.isEnabled = state.isEnabled
        rankingsPollingRingtonePreference.ringtoneUri = state.ringtoneUri
        rankingsPollingRingtonePreference.isEnabled = state.isEnabled
        rankingsPollingVibratePreference.isChecked = state.isVibrationEnabled
        rankingsPollingVibratePreference.isEnabled = state.isEnabled
        rankingsPollingWifiPreference.isChecked = state.isWifiRequired
        rankingsPollingWifiPreference.isEnabled = state.isEnabled
        rankingsPollingChargingPreference.isChecked = state.isChargingRequired
        rankingsPollingChargingPreference.isEnabled = state.isEnabled
    }

    private fun refreshSmashRosterState(state: SmashRosterState) {
        when (state) {
            is SmashRosterState.Fetched -> {
                smashRosterPreference.setContent(state.result)
            }

            is SmashRosterState.Fetching -> smashRosterPreference.setLoading()
            is SmashRosterState.Syncing -> smashRosterPreference.setSyncing()
        }
    }

    private fun saveSmashRosterRingtone(data: Intent?) {
        val ringtoneUri: AndroidUri? = data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
        viewModel.setRankingsPollingRingtoneUri(ringtoneUri?.toString())
    }

    private val deleteFavoritePlayersListener = object : DeleteFavoritePlayersPreferenceView.Listener {
        override fun onDeleteFavoritePlayersClick(v: DeleteFavoritePlayersPreferenceView) {
            viewModel.deleteFavoritePlayers()
        }
    }

    private val identityPreferenceListeners = object : IdentityPreferenceView.Listeners {
        override fun onDeleteIdentityClick(v: IdentityPreferenceView) {
            viewModel.deleteIdentity()
        }

        override fun onSetIdentityClick(v: IdentityPreferenceView) {
            startActivityForResult(SetIdentityActivity.getLaunchIntent(this@SettingsActivity),
                    RequestCodes.CHANGE_IDENTITY.value)
        }
    }

    private val nightModePreferenceListener = object : NightModePreferenceView.Listener {
        override fun onNightModeChange(v: NightModePreferenceView, nightMode: NightMode) {
            viewModel.setNightMode(nightMode)

            startActivity(HomeActivity.getLaunchIntent(
                    context = this@SettingsActivity,
                    restartActivityTask = true
            ))
        }
    }

    private val rankingsPollingPollFrequencyListener = object : PollFrequencyPreferenceView.Listener {
        override fun onPollFrequencyChange(v: PollFrequencyPreferenceView,
                pollFrequency: PollFrequency) {
            viewModel.setRankingsPollingPollFrequency(pollFrequency)
        }
    }

    private val rankingsPollingChargingListener = object : CheckablePreferenceView.Listener {
        override fun onClick(v: CheckablePreferenceView) {
            viewModel.setRankingsPollingIsChargingRequired(!v.isChecked)
        }
    }

    private val rankingsPollingEnabledListener = object : CheckablePreferenceView.Listener {
        override fun onClick(v: CheckablePreferenceView) {
            viewModel.setRankingsPollingIsEnabled(!v.isChecked)
        }
    }

    private val rankingsPollingVibrateListener = object : CheckablePreferenceView.Listener {
        override fun onClick(v: CheckablePreferenceView) {
            if (!v.isChecked) {
                // the user is turning vibration ON, let's give them a cute lil nudge
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            }

            viewModel.setRankingsPollingIsVibrationEnabled(!v.isChecked)
        }
    }

    private val rankingsPollingWifiListener = object : CheckablePreferenceView.Listener {
        override fun onClick(v: CheckablePreferenceView) {
            viewModel.setRankingsPollingIsWifiRequired(!v.isChecked)
        }
    }

    private val regionPreferenceListener = object : RegionPreferenceView.Listener {
        override fun onClick(v: RegionPreferenceView) {
            startActivityForResult(SetRegionActivity.getLaunchIntent(this@SettingsActivity),
                    RequestCodes.CHANGE_REGION.value)
        }
    }

    private val ringtonePreferenceListener = object : RingtonePreferenceView.Listener {
        override fun onClick(v: RingtonePreferenceView) {
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
                    .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
                    .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
                    .putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)

            v.ringtoneUri?.let { javaUri ->
                val androidUri = AndroidUri.parse(javaUri.toString())
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, androidUri)
            }

            try {
                startActivityForResult(intent, RequestCodes.CHANGE_RINGTONE.value)
            } catch (e: ActivityNotFoundException) {
                timber.e(TAG, "Unable to start ringtone picker Activity", e)
                Toast.makeText(this@SettingsActivity,
                        R.string.unable_to_launch_ringtone_picker, Toast.LENGTH_LONG).show()
            }
        }
    }

    private val smashRosterPreferenceListener = object : SmashRosterSyncPreferenceView.Listener {
        override fun onClick(v: SmashRosterSyncPreferenceView) {
            viewModel.syncSmashRoster()
        }
    }

}

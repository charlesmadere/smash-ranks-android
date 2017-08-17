package com.garpr.android.preferences

import android.net.Uri

import com.garpr.android.models.PollFrequency
import com.garpr.android.models.SimpleDate

interface RankingsPollingPreferenceStore : BasePreferenceStore {

    val chargingRequired: Preference<Boolean>

    val enabled: Preference<Boolean>

    val lastPoll: Preference<SimpleDate>

    val pollFrequency: Preference<PollFrequency>

    val rankingsDate: Preference<SimpleDate>

    val ringtone: Preference<Uri>

    val vibrationEnabled: Preference<Boolean>

    val wifiRequired: Preference<Boolean>

}

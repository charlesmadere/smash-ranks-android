package com.garpr.android.preferences

import com.garpr.android.data.models.PollFrequency
import com.garpr.android.data.models.SimpleDate
import java.net.URI as JavaUri

interface RankingsPollingPreferenceStore : PreferenceStore {

    val chargingRequired: Preference<Boolean>

    val enabled: Preference<Boolean>

    val ringtone: Preference<JavaUri>

    val lastPoll: Preference<SimpleDate>

    val pollFrequency: Preference<PollFrequency>

    val rankingsId: Preference<String>

    val vibrationEnabled: Preference<Boolean>

    val wifiRequired: Preference<Boolean>

}

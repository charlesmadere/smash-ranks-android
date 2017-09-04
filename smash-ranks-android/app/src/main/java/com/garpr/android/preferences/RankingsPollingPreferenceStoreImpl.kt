package com.garpr.android.preferences

import com.garpr.android.models.PollFrequency
import com.garpr.android.models.SimpleDate
import com.garpr.android.preferences.persistent.PersistentBooleanPreference
import com.garpr.android.preferences.persistent.PersistentGsonPreference
import com.garpr.android.preferences.persistent.PersistentUriPreference
import com.google.gson.Gson

class RankingsPollingPreferenceStoreImpl(
        gson: Gson,
        keyValueStore: KeyValueStore
) : BasePreferenceStore(
        keyValueStore
), RankingsPollingPreferenceStore {

    override val chargingRequired = PersistentBooleanPreference("CHARGING_REQUIRED",
            false, keyValueStore)

    override val enabled = PersistentBooleanPreference("ENABLED", true,
            keyValueStore)

    override val lastPoll = PersistentGsonPreference<SimpleDate>("LAST_POLL",
            null, keyValueStore, SimpleDate::class.java, gson)

    override val pollFrequency = PersistentGsonPreference("POLL_FREQUENCY",
            PollFrequency.DAILY, keyValueStore, PollFrequency::class.java, gson)

    override val rankingsDate = PersistentGsonPreference<SimpleDate>("RANKINGS_DATE",
            null, keyValueStore, SimpleDate::class.java, gson)

    override val ringtone = PersistentUriPreference("RINGTONE", null,
            keyValueStore)

    override val vibrationEnabled = PersistentBooleanPreference("VIBRATION_ENABLED",
            false, keyValueStore)

    override val wifiRequired = PersistentBooleanPreference("WIFI_REQUIRED",
            true, keyValueStore)

}

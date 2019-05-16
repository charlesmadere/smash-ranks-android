package com.garpr.android.preferences

import com.garpr.android.data.models.PollFrequency
import com.garpr.android.data.models.SimpleDate
import com.garpr.android.preferences.persistent.PersistentBooleanPreference
import com.garpr.android.preferences.persistent.PersistentMoshiPreference
import com.garpr.android.preferences.persistent.PersistentStringPreference
import com.garpr.android.preferences.persistent.PersistentUriPreference
import com.squareup.moshi.Moshi

class RankingsPollingPreferenceStoreImpl(
        override val keyValueStore: KeyValueStore,
        moshi: Moshi
) : RankingsPollingPreferenceStore {

    override val chargingRequired by lazy { PersistentBooleanPreference("CHARGING_REQUIRED",
            false, keyValueStore) }

    override val enabled by lazy { PersistentBooleanPreference("ENABLED", true,
            keyValueStore) }

    override val lastPoll by lazy { PersistentMoshiPreference<SimpleDate>("LAST_POLL",
            null, keyValueStore, moshi, SimpleDate::class.java) }

    override val pollFrequency by lazy { PersistentMoshiPreference("POLL_FREQUENCY",
            PollFrequency.DAILY, keyValueStore, moshi, PollFrequency::class.java) }

    override val rankingsId by lazy { PersistentStringPreference("RANKINGS_ID",
            null, keyValueStore) }

    override val ringtone by lazy { PersistentUriPreference("RINGTONE", null,
            keyValueStore) }

    override val vibrationEnabled by lazy { PersistentBooleanPreference("VIBRATION_ENABLED",
            false, keyValueStore) }

    override val wifiRequired by lazy { PersistentBooleanPreference("WIFI_REQUIRED",
            true, keyValueStore) }

}

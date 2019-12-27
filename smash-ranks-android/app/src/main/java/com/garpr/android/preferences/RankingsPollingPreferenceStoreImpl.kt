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

    override val chargingRequired by lazy {
        PersistentBooleanPreference(
                key = "CHARGING_REQUIRED",
                defaultValue = false,
                keyValueStore = keyValueStore
        )
    }

    override val enabled by lazy {
        PersistentBooleanPreference(
                key = "ENABLED",
                defaultValue = true,
                keyValueStore = keyValueStore
        )
    }

    override val lastPoll by lazy {
        PersistentMoshiPreference<SimpleDate>(
                key = "LAST_POLL",
                defaultValue = null,
                keyValueStore = keyValueStore,
                jsonAdapter = moshi.adapter(SimpleDate::class.java)
        )
    }

    override val pollFrequency by lazy {
        PersistentMoshiPreference(
                key = "POLL_FREQUENCY",
                defaultValue = PollFrequency.DAILY,
                keyValueStore = keyValueStore,
                jsonAdapter = moshi.adapter(PollFrequency::class.java)
        )
    }

    override val rankingsId by lazy {
        PersistentStringPreference(
                key = "RANKINGS_ID",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
    }

    override val ringtone by lazy {
        PersistentUriPreference(
                key = "RINGTONE",
                defaultValue = null,
                keyValueStore = keyValueStore
        )
    }

    override val vibrationEnabled by lazy {
        PersistentBooleanPreference(
                key = "VIBRATION_ENABLED",
                defaultValue = false,
                keyValueStore = keyValueStore
        )
    }

    override val wifiRequired by lazy {
        PersistentBooleanPreference(
                key = "WIFI_REQUIRED",
                defaultValue = true,
                keyValueStore = keyValueStore
        )
    }

}

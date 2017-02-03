package com.garpr.android.preferences;

import com.garpr.android.models.PollFrequency;

public interface RankingsPollingPreferenceStore {

    void clear();

    Preference<Boolean> getChargingRequired();

    Preference<Boolean> getEnabled();

    Preference<Long> getLastPoll();

    Preference<PollFrequency> getPollFrequency();

    Preference<Boolean> getWifiRequired();

}

package com.garpr.android.preferences;

import com.garpr.android.models.PollFrequency;
import com.garpr.android.models.SimpleDate;

public interface RankingsPollingPreferenceStore {

    void clear();

    Preference<Boolean> getChargingRequired();

    Preference<Boolean> getEnabled();

    Preference<SimpleDate> getLastPoll();

    Preference<PollFrequency> getPollFrequency();

    Preference<SimpleDate> getRankingsDate();

    Preference<Boolean> getVibrationEnabled();

    Preference<Boolean> getWifiRequired();

}

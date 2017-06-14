package com.garpr.android.preferences;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.garpr.android.models.PollFrequency;
import com.garpr.android.models.SimpleDate;
import com.garpr.android.preferences.persistent.BasePreferenceStore;

public interface RankingsPollingPreferenceStore extends BasePreferenceStore {

    @NonNull
    Preference<Boolean> getChargingRequired();

    @NonNull
    Preference<Boolean> getEnabled();

    @NonNull
    Preference<SimpleDate> getLastPoll();

    @NonNull
    Preference<PollFrequency> getPollFrequency();

    @NonNull
    Preference<SimpleDate> getRankingsDate();

    @NonNull
    Preference<Uri> getRingtone();

    @NonNull
    Preference<Boolean> getVibrationEnabled();

    @NonNull
    Preference<Boolean> getWifiRequired();

}

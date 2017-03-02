package com.garpr.android.preferences;

import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.NightMode;

public interface GeneralPreferenceStore {

    void clear();

    Preference<String> getCurrentRegion();

    Preference<AbsPlayer> getIdentity();

    Preference<Integer> getLastVersion();

    Preference<NightMode> getNightMode();

}

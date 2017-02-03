package com.garpr.android.preferences;

import com.garpr.android.models.NightMode;

public interface GeneralPreferenceStore {

    void clear();

    Preference<String> getCurrentRegion();

    Preference<Integer> getLastVersion();

    Preference<NightMode> getNightMode();

}

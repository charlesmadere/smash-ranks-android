package com.garpr.android.preferences;

import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.NightMode;
import com.garpr.android.models.Region;

public interface GeneralPreferenceStore {

    void clear();

    Preference<Region> getCurrentRegion();

    Preference<AbsPlayer> getIdentity();

    Preference<Integer> getLastVersion();

    Preference<NightMode> getNightMode();

}

package com.garpr.android.preferences;

import com.garpr.android.models.FavoritePlayer;
import com.garpr.android.models.NightMode;
import com.garpr.android.models.Region;

public interface GeneralPreferenceStore {

    void clear();

    Preference<Region> getCurrentRegion();

    Preference<FavoritePlayer> getIdentity();

    Preference<Integer> getLastVersion();

    Preference<NightMode> getNightMode();

}

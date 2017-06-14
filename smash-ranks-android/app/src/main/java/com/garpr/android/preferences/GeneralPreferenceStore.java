package com.garpr.android.preferences;

import android.support.annotation.NonNull;

import com.garpr.android.models.FavoritePlayer;
import com.garpr.android.models.NightMode;
import com.garpr.android.models.Region;

public interface GeneralPreferenceStore extends BasePreferenceStore {

    @NonNull
    Preference<Region> getCurrentRegion();

    @NonNull
    Preference<FavoritePlayer> getIdentity();

    @NonNull
    Preference<Integer> getLastVersion();

    @NonNull
    Preference<NightMode> getNightMode();

}

package com.garpr.android.preferences;

import com.garpr.android.models.NightMode;

public interface PreferenceStore {

    void clearAll();
    Preference<NightMode> getNightMode();

}

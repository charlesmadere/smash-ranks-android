package com.garpr.android.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public interface PreferenceStore {

    void clearAll();
    Context getContext();
    SharedPreferences getDefaultSharedPreferences();

}

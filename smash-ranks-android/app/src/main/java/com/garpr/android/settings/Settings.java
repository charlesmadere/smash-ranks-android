package com.garpr.android.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.garpr.android.App;

import java.util.Map;
import java.util.Set;


public final class Settings {


    private static final String CNAME = "com.garpr.android.settings.Settings";

    public static final BooleanSetting OnboardingComplete;
    public static final IntegerSetting LastVersion;
    public static final LongSetting RankingsDate;
    public static final RegionSetting Region;
    public static final SyncSetting Sync;
    public static final UserSetting User;




    static {
        LastVersion = new IntegerSetting(CNAME, "LAST_VERSION");
        OnboardingComplete = new BooleanSetting(CNAME, "ONBOARDING_COMPLETE");
        RankingsDate = new LongSetting(CNAME, "RANKINGS_DATE");
        Region = new RegionSetting(CNAME, "REGION");
        Sync = new SyncSetting(CNAME, "SYNC");
        User = new UserSetting(CNAME, "USER");
    }


    public static boolean areWeInTheUsersRegion() {
        return Region.get().equals(User.Region.get());
    }


    private static void delete(final SharedPreferences sPreferences) {
        final Map<String, ?> all = sPreferences.getAll();

        if (all != null && !all.isEmpty()) {
            final Editor editor = sPreferences.edit();
            final Set<String> keys = all.keySet();

            if (!keys.isEmpty()) {
                for (final String key : keys) {
                    editor.remove(key);
                }

                editor.apply();
            }
        }
    }


    private static void delete(final String... cnames) {
        if (cnames != null && cnames.length >= 1) {
            for (final String cname : cnames) {
                delete(get(cname));
            }
        }

        delete(PreferenceManager.getDefaultSharedPreferences(App.getContext()));
    }


    public static void deleteAll() {
        delete(CNAME);
    }


    public static Editor edit(final String name) {
        return get(name).edit();
    }


    public static SharedPreferences get(final String name) {
        final Context context = App.getContext();
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }


}

package com.garpr.android.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.garpr.android.App;

import java.util.Map;
import java.util.Set;


public final class Settings {


    private static final String BASE_CNAME = "com.garpr.android.settings.Settings";

    public static final BooleanSetting OnboardingComplete;
    public static final IntegerSetting LastVersion;
    public static final LongSetting RankingsDate;
    public static final RegionSetting Region;




    static {
        LastVersion = new IntegerSetting(BASE_CNAME, "LAST_VERSION", 0);
        OnboardingComplete = new BooleanSetting(BASE_CNAME, "ONBOARDING_COMPLETE", false);
        RankingsDate = new LongSetting(BASE_CNAME, "RANKINGS_DATE", 0L);
        Region = new RegionSetting(BASE_CNAME, "REGION_SETTING");
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
        delete(BASE_CNAME, Sync.CNAME, User.CNAME);
    }


    public static Editor edit(final String name) {
        return get(name).edit();
    }


    public static SharedPreferences get(final String name) {
        final Context context = App.getContext();
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }




    public static final class Sync {


        private static final String CNAME = BASE_CNAME + ".Sync";

        public static final BooleanSetting ChargingIsNecessary;
        public static final BooleanSetting IsEnabled;
        public static final BooleanSetting IsScheduled;
        public static final BooleanSetting WifiIsNecessary;
        public static final LongSetting LastDate;


        static {
            ChargingIsNecessary = new BooleanSetting(CNAME, "CHARGING_IS_NECESSARY", false);
            IsEnabled = new BooleanSetting(CNAME, "IS_ENABLED", true);
            IsScheduled = new BooleanSetting(CNAME, "IS_SCHEDULED", false);
            LastDate = new LongSetting(CNAME, "LAST_DATE", 0L);
            WifiIsNecessary = new BooleanSetting(CNAME, "WIFI_IS_NECESSARY", true);
        }


    }


    public static final class User {


        private static final String CNAME = BASE_CNAME + ".User";

        public static final IntegerSetting Rank;
        public static final PlayerSetting Player;
        public static final RegionSetting Region;


        static {
            Rank = new IntegerSetting(CNAME, "RANK", 0);
            Player = new PlayerSetting(CNAME, "PLAYER");
            Region = new RegionSetting(CNAME, "REGION");
        }


        public static boolean areWeInTheUsersRegion() {
            return Region.get().equals(Settings.Region.get());
        }


        public static boolean hasPlayer() {
            return Player.get() != null;
        }


    }


}

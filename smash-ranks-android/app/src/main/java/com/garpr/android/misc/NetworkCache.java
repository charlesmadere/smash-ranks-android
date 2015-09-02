package com.garpr.android.misc;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.format.DateUtils;

import com.garpr.android.BuildConfig;
import com.garpr.android.settings.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;


public final class NetworkCache {


    private static final int MAX_SIZE;
    private static final long TIMESTAMP_CUTOFF = DateUtils.DAY_IN_MILLIS * 5L; // 5 days
    private static final String CNAME = "com.garpr.android.misc.NetworkCache";
    private static final String JSON = CNAME + ".JSON";
    private static final String TAG = "NetworkCache";
    private static final String TIMESTAMPS = CNAME + ".TIMESTAMPS";

    private static final Comparator<String> TIMESTAMP_COMPARATOR = new Comparator<String>() {
        @Override
        public int compare(final String lhs, final String rhs) {
            final int lhsTime = (int) (Long.parseLong(lhs) / 1000L);
            final int rhsTime = (int) (Long.parseLong(rhs) / 1000L);
            return lhsTime - rhsTime;
        }
    };




    static {
        if (BuildConfig.DEBUG) {
            MAX_SIZE = 6;
        } else {
            MAX_SIZE = 24;
        }
    }


    @SuppressWarnings("unchecked")
    private static synchronized void clean() {
        final SharedPreferences timestampsCache = getTimestampsCache();
        final Map<String, String> timestampsMap = (Map<String, String>) timestampsCache.getAll();

        if (timestampsMap == null || timestampsMap.isEmpty()) {
            return;
        }

        final long now = System.currentTimeMillis() / 1000L;
        final Editor timestampsCacheEditor = timestampsCache.edit();
        final Editor jsonCacheEditor = getJsonCache().edit();

        final LinkedList<String> timestampsList = new LinkedList<>(timestampsMap.keySet());
        Collections.sort(timestampsList, TIMESTAMP_COMPARATOR);

        while (timestampsList.size() >= MAX_SIZE) {
            final String timestampToRemove = timestampsList.removeLast();
            final String urlToRemove = timestampsMap.get(timestampToRemove);
            timestampsCacheEditor.remove(timestampToRemove);
            jsonCacheEditor.remove(urlToRemove);
        }

        for (final String timestamp : timestampsList) {
            final long then = Long.parseLong(timestamp);

            if (now - then >= TIMESTAMP_CUTOFF) {
                jsonCacheEditor.remove(timestampsMap.get(timestamp));
                timestampsCacheEditor.remove(timestamp);
            }
        }

        timestampsCacheEditor.apply();
        jsonCacheEditor.apply();
    }


    public static synchronized void clear() {
        final SharedPreferences timestampsCache = getTimestampsCache();
        Console.d(TAG, "Clearing network cache, it had " + timestampsCache.getAll().size() + " entries");
        timestampsCache.edit().clear().apply();
        getJsonCache().edit().clear().apply();
    }


    public static synchronized JSONObject get(final String url) {
        final SharedPreferences jsonCache = getJsonCache();
        final String jsonString = jsonCache.getString(url, null);
        final JSONObject json;

        if (Utils.validStrings(jsonString)) {
            try {
                json = new JSONObject(jsonString);
            } catch (final JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            json = null;
        }

        return json;
    }


    private static SharedPreferences getJsonCache() {
        return Settings.get(JSON);
    }


    private static SharedPreferences getTimestampsCache() {
        return Settings.get(TIMESTAMPS);
    }


    @SuppressWarnings("unchecked")
    public synchronized static void set(final String url, final JSONObject json) {
        clean();

        final SharedPreferences timestampsCache = getTimestampsCache();
        final Map<String, String> timestamps = (Map<String, String>) timestampsCache.getAll();
        final Editor timestampsCacheEditor = timestampsCache.edit();
        final Editor jsonCacheEditor = getJsonCache().edit();

        if (timestamps != null && !timestamps.isEmpty()) {
            for (final Entry<String, String> timestamp : timestamps.entrySet()) {
                final String timestampUrl = timestamp.getValue();

                if (url.equalsIgnoreCase(timestampUrl)) {
                    timestampsCacheEditor.remove(timestamp.getKey());
                    jsonCacheEditor.remove(timestampUrl);
                    break;
                }
            }
        }

        final String timestamp = String.valueOf(System.currentTimeMillis());
        timestampsCacheEditor.putString(timestamp, url);
        timestampsCacheEditor.apply();

        jsonCacheEditor.putString(url, json.toString());
        jsonCacheEditor.apply();
    }


    @SuppressWarnings("unchecked")
    public synchronized static int size() {
        final SharedPreferences timestampsCache = getTimestampsCache();
        final Map<String, String> timestamps = (Map<String, String>) timestampsCache.getAll();
        final int timestampsSize = timestamps == null ? 0 : timestamps.size();

        final SharedPreferences jsonCache = getJsonCache();
        final Map<String, String> jsons = (Map<String, String>) jsonCache.getAll();
        final int jsonSize = jsons == null ? 0 : jsons.size();

        if (timestampsSize == jsonSize) {
            return timestampsSize;
        } else {
            throw new IllegalStateException("timestampsCache (" + timestampsSize + ") and "
                    + " jsonCache (" + jsonSize + ") have out-of-sync sizes!");
        }
    }


}

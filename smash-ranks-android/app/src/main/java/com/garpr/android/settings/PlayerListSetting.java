package com.garpr.android.settings;


import com.garpr.android.misc.Console;
import com.garpr.android.models.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public final class PlayerListSetting extends Setting<ArrayList<Player>> {


    private static final String TAG = "PlayerListSetting";

    private final JSONArraySetting mJSONArraySetting;




    public PlayerListSetting(final String name, final String key) {
        super(name, key);
        mJSONArraySetting = new JSONArraySetting(name, key);
    }


    @Override
    public boolean exists() {
        return super.exists() || mJSONArraySetting.exists();
    }


    @Override
    public ArrayList<Player> get() {
        ArrayList<Player> players = null;

        if (exists()) {
            try {
                final JSONArray playersJSON = mJSONArraySetting.get();
                final int jsonLength = playersJSON.length();
                players = new ArrayList<>(jsonLength);

                for (int i = 0; i < jsonLength; ++i) {
                    final JSONObject playerJSON = playersJSON.getJSONObject(i);
                    players.add(new Player(playerJSON));
                }
            } catch (final JSONException e) {
                Console.e(TAG, "JSONException when reading in stored PlayerListSetting data", e);
                players = null;
            }
        }

        return players;
    }


    @Override
    public void set(final ArrayList<Player> newValue, final boolean notifyListeners) {
        final JSONArray jsonArray = Player.toJSON(newValue);
        mJSONArraySetting.set(jsonArray, notifyListeners);
        super.set(newValue, notifyListeners);
    }


}

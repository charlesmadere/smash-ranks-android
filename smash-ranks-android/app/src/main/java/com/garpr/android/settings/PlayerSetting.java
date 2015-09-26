package com.garpr.android.settings;


import com.garpr.android.models.Player;

import org.json.JSONException;
import org.json.JSONObject;


public final class PlayerSetting extends Setting<Player> {


    private final JSONObjectSetting mJSONObjectSetting;




    public PlayerSetting(final String name, final String key) {
        super(name, key);
        mJSONObjectSetting = new JSONObjectSetting(name, key);
    }


    @Override
    public boolean exists() {
        return super.exists() && mJSONObjectSetting.exists();
    }


    @Override
    public Player get() {
        final JSONObject json = mJSONObjectSetting.get();

        try {
            final Player player;

            if (json == null) {
                player = null;
            } else {
                player = new Player(json);
            }

            return player;
        } catch (final JSONException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void set(final Player newValue, final boolean notifyListeners) {
        mJSONObjectSetting.set(newValue.toJSON(), notifyListeners);
        super.set(newValue, notifyListeners);
    }


}

package com.garpr.android.calls;


import com.garpr.android.misc.Constants;
import com.garpr.android.models.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public final class Players {


    public static void get(final Response<ArrayList<Player>> response, final boolean ignoreCache) {
        new PlayersCall(response, ignoreCache).make();
    }




    private static final class PlayersCall extends RegionBasedCall<ArrayList<Player>> {


        private static final String TAG = "PlayersCall";


        private PlayersCall(final Response<ArrayList<Player>> response, final boolean ignoreCache)
                throws IllegalArgumentException {
            super(response, ignoreCache);
        }


        @Override
        String getCallName() {
            return TAG;
        }


        @Override
        String getUrl() {
            return super.getUrl() + Constants.PLAYERS;
        }


        @Override
        void onJSONResponse(final JSONObject json) throws JSONException {
            final JSONArray playersJSON = json.getJSONArray(Constants.PLAYERS);
            final int playersLength = playersJSON.length();
            final ArrayList<Player> players = new ArrayList<>(playersLength);

            for (int i = 0; i < playersLength; ++i) {
                final JSONObject playerJSON = playersJSON.getJSONObject(i);
                final Player player = new Player(playerJSON);
                players.add(player);
            }

            mResponse.success(players);
        }


    }


}

package com.garpr.android.calls;


import com.garpr.android.misc.Constants;
import com.garpr.android.models.HeadToHeadBundle;
import com.garpr.android.models.Match;
import com.garpr.android.models.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public final class Matches {


    public static void get(final Response<ArrayList<Match>> response, final Player player,
            final boolean ignoreCache) {
        new MatchesCall(response, player, ignoreCache).make();
    }


    public static void getHeadToHead(final Response<HeadToHeadBundle> response,
            final Player player, final Player opponent, final boolean ignoreCache) {
        new HeadToHeadCall(response, player, opponent, ignoreCache).make();
    }




    private static final class HeadToHeadCall extends RegionBasedCall<HeadToHeadBundle> {


        private static final String TAG = "HeadToHeadCall";

        private final Player mOpponent;
        private final Player mPlayer;


        private HeadToHeadCall(final Response<HeadToHeadBundle> response, final Player player,
                final Player opponent, final boolean ignoreCache) throws IllegalArgumentException {
            super(response, ignoreCache);

            if (player == null) {
                throw new IllegalArgumentException("player is null");
            } else if (opponent == null) {
                throw new IllegalArgumentException("opponent is null");
            }

            mPlayer = player;
            mOpponent = opponent;
        }


        @Override
        String getCallName() {
            return TAG;
        }


        @Override
        String getUrl() {
            return super.getUrl() + Constants.MATCHES + '/' + mPlayer.getId() + '?' +
                    Constants.OPPONENT + '=' + mOpponent.getId();
        }


        @Override
        void onJSONResponse(final JSONObject json) throws JSONException {
            final HeadToHeadBundle headToHeadBundle = new HeadToHeadBundle(json);
            mResponse.success(headToHeadBundle);
        }


    }


    private static final class MatchesCall extends RegionBasedCall<ArrayList<Match>> {


        private static final String TAG = "MatchesCall";

        private final Player mPlayer;


        private MatchesCall(final Response<ArrayList<Match>> response, final Player player,
                final boolean ignoreCache) throws IllegalArgumentException {
            super(response, ignoreCache);

            if (player == null) {
                throw new IllegalArgumentException("player is null");
            }

            mPlayer = player;
        }


        @Override
        String getCallName() {
            return TAG;
        }


        @Override
        String getUrl() {
            return super.getUrl() + Constants.MATCHES + '/' + mPlayer.getId();
        }


        @Override
        void onJSONResponse(final JSONObject json) throws JSONException {
            final JSONArray matchesJSON = json.getJSONArray(Constants.MATCHES);
            final int matchesLength = matchesJSON.length();
            final ArrayList<Match> matches = new ArrayList<>(matchesLength);

            for (int i = 0; i < matchesLength; ++i) {
                final JSONObject matchJSON = matchesJSON.getJSONObject(i);
                final Match match = new Match(matchJSON, mPlayer);
                matches.add(match);
            }

            mResponse.success(matches);
        }


    }


}

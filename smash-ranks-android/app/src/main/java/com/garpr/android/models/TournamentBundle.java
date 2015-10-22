package com.garpr.android.models;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TournamentBundle implements Parcelable {


    private final ArrayList<Match> mMatches;
    private final ArrayList<Player> mPlayers;
    private final Tournament mTournament;




    private static Player findPlayerById(final ArrayList<Player> players, final String id) {
        for (final Player player : players) {
            if (id.equals(player.getId())) {
                return player;
            }
        }

        // this should never happen
        throw new RuntimeException("unable to find player with id \"" + id + '"');
    }


    public TournamentBundle(final JSONObject json) throws JSONException {
        mTournament = new Tournament(json);

        final JSONArray playersJSON = json.getJSONArray(Constants.PLAYERS);
        final int playersLength = playersJSON.length();
        mPlayers = new ArrayList<>(playersLength);

        for (int i = 0; i < playersLength; ++i) {
            final JSONObject playerJSON = playersJSON.getJSONObject(i);
            mPlayers.add(new Player(playerJSON));
        }

        final JSONArray matchesJSON = json.getJSONArray(Constants.MATCHES);
        final int matchesLength = matchesJSON.length();
        mMatches = new ArrayList<>(matchesLength);

        for (int i = matchesLength - 1; i >= 0; --i) {
            final JSONObject matchJSON = matchesJSON.getJSONObject(i);
            final String loserId = matchJSON.getString(Constants.LOSER_ID);
            final Player loser = findPlayerById(mPlayers, loserId);

            final String winnerId = matchJSON.getString(Constants.WINNER_ID);
            final Player winner = findPlayerById(mPlayers, winnerId);

            mMatches.add(new Match(loser, winner, mTournament));
        }
    }


    private TournamentBundle(final Parcel source) {
        mTournament = source.readParcelable(Tournament.class.getClassLoader());
        mMatches = source.createTypedArrayList(Match.CREATOR);
        mPlayers = source.createTypedArrayList(Player.CREATOR);
    }


    @Override
    public boolean equals(final Object o) {
        final boolean isEqual;

        if (this == o) {
            isEqual = true;
        } else if (o instanceof TournamentBundle) {
            final TournamentBundle tb = (TournamentBundle) o;
            isEqual = mMatches.equals(tb.getMatches()) && mPlayers.equals(tb.getPlayers()) &&
                    mTournament.equals(tb.getTournament());
        } else {
            isEqual = false;
        }

        return isEqual;
    }


    public ArrayList<Match> getMatches() {
        return mMatches;
    }


    public ArrayList<Player> getPlayers() {
        return mPlayers;
    }


    public Tournament getTournament() {
        return mTournament;
    }


    @Override
    public int hashCode() {
        // this method was automatically generated by Android Studio

        int result = mMatches.hashCode();
        result = 31 * result + mPlayers.hashCode();
        result = 31 * result + mTournament.hashCode();

        return result;
    }


    @Override
    public String toString() {
        final Context context = App.get();
        return context.getString(R.string.w_on_x_has_y_players_and_z_matches, mTournament.getName(),
                mTournament.getDateWrapper().getMmDdYy(), mPlayers.size(), mMatches.size());
    }




    /*
     * Code necessary for the Android Parcelable interface is below. Read more here:
     * https://developer.android.com/intl/es/reference/android/os/Parcelable.html
     */


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(mTournament, flags);
        dest.writeTypedList(mMatches);
        dest.writeTypedList(mPlayers);
    }


    public static final Creator<TournamentBundle> CREATOR = new Creator<TournamentBundle>() {
        @Override
        public TournamentBundle createFromParcel(final Parcel source) {
            return new TournamentBundle(source);
        }


        @Override
        public TournamentBundle[] newArray(final int size) {
            return new TournamentBundle[size];
        }
    };


}

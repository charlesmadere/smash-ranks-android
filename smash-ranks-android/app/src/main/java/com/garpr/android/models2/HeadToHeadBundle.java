package com.garpr.android.models2;


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


public class HeadToHeadBundle implements Parcelable {


    private final ArrayList<Match> mMatches;
    private final int mLosses;
    private final int mWins;
    private final Player mOpponent;
    private final Player mPlayer;




    public HeadToHeadBundle(final JSONObject json) throws JSONException {
        mLosses = json.getInt(Constants.LOSSES);
        mWins = json.getInt(Constants.WINS);

        final JSONObject opponentJSON = json.getJSONObject(Constants.OPPONENT);
        mOpponent = new Player(opponentJSON);

        final JSONObject playerJSON = json.getJSONObject(Constants.PLAYER);
        mPlayer = new Player(playerJSON);

        final JSONArray matchesJSON = json.getJSONArray(Constants.MATCHES);
        final int matchesLength = matchesJSON.length();
        mMatches = new ArrayList<>(matchesLength);

        for (int i = 0; i < matchesLength; ++i) {
            final JSONObject matchJSON = matchesJSON.getJSONObject(i);
            final Match match = new Match(matchJSON, mPlayer);
            mMatches.add(match);
        }
    }


    private HeadToHeadBundle(final Parcel source) {
        mLosses = source.readInt();
        mWins = source.readInt();
        mOpponent = source.readParcelable(Player.class.getClassLoader());
        mPlayer = source.readParcelable(Player.class.getClassLoader());
        mMatches = source.createTypedArrayList(Match.CREATOR);
    }


    public int getLosses() {
        return mLosses;
    }


    public ArrayList<Match> getMatches() {
        return mMatches;
    }


    public Player getOpponent() {
        return mOpponent;
    }


    public Player getPlayer() {
        return mPlayer;
    }


    public int getWins() {
        return mWins;
    }


    @Override
    public String toString() {
        final Context context = App.getContext();
        return context.getString(R.string.w_vs_x_wins_losses, mPlayer.getName(),
                mOpponent.getName(), mWins, mLosses);
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
        dest.writeInt(mLosses);
        dest.writeInt(mWins);
        dest.writeParcelable(mOpponent, flags);
        dest.writeParcelable(mPlayer, flags);
        dest.writeTypedList(mMatches);
    }


    public static final Creator<HeadToHeadBundle> CREATOR = new Creator<HeadToHeadBundle>() {
        @Override
        public HeadToHeadBundle createFromParcel(final Parcel source) {
            return new HeadToHeadBundle(source);
        }


        @Override
        public HeadToHeadBundle[] newArray(final int size) {
            return new HeadToHeadBundle[size];
        }
    };


}

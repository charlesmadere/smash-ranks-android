package com.garpr.android.fragments;


import android.content.Context;
import android.support.v4.app.Fragment;

import com.garpr.android.misc.ListUtils.SpecialFilterable;
import com.garpr.android.models.Match;
import com.garpr.android.models.Player;
import com.garpr.android.models.Tournament;


public class PlayerActivityDataFragment extends DataFragment {


    private static final String TAG = "PlayerActivityDataFragment";

    private Listeners mListeners;




    public static PlayerActivityDataFragment create() {
        return (PlayerActivityDataFragment) create(new PlayerActivityDataFragment());
    }


    public void getPlayers(final Listeners listeners) {
        // TODO
    }


    @Override
    protected String getFragmentName() {
        return TAG;
    }


    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        if (context instanceof Listeners) {
            mListeners = (Listeners) context;
        } else {
            final Fragment fragment = getParentFragment();

            if (fragment instanceof Listeners) {
                mListeners = (Listeners) fragment;
            } else {
                throw new RuntimeException(TAG + " unable to attach to Listeners");
            }
        }
    }


    @Override
    public void onDetach() {
        mListeners = null;
        super.onDetach();
    }




    public static abstract class ListItem implements SpecialFilterable {


        private static long sId;
        final long mId;


        private ListItem() {
            mId = sId++;
        }


        @Override
        public final String toString() {
            return getName();
        }


    }


    public static class PlayerListItem extends ListItem {


        private final Match mMatch;
        private final Player mOpponent;


        private PlayerListItem(final Match match, final Player player) {
            mMatch = match;
            mOpponent = match.getOtherPlayer(player);
        }


        @Override
        public String getName() {
            return mOpponent.getName();
        }


        @Override
        public boolean isBasicItem() {
            return true;
        }


        @Override
        public boolean isSpecialItem() {
            return false;
        }


    }


    public static class TournamentListItem extends ListItem {


        private final Tournament mTournament;


        private TournamentListItem(final Tournament tournament) {
            mTournament = tournament;
        }


        @Override
        public String getName() {
            return mTournament.getName();
        }


        @Override
        public boolean isBasicItem() {
            return false;
        }


        @Override
        public boolean isSpecialItem() {
            return true;
        }


    }


    public interface Listeners {





    }


}

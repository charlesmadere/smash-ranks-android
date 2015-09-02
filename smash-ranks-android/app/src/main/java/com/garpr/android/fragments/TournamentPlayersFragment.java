package com.garpr.android.fragments;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.garpr.android.R;
import com.garpr.android.activities.PlayerActivity;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.ListUtils.AlphabeticalSectionCreator;
import com.garpr.android.misc.ListUtils.AlphabeticallyComparable;
import com.garpr.android.models.Player;
import com.garpr.android.models.TournamentBundle;
import com.garpr.android.settings.Settings.User;
import com.garpr.android.views.PlayerItemView;
import com.garpr.android.views.SimpleSeparatorView;

import java.util.ArrayList;
import java.util.Collections;


public class TournamentPlayersFragment extends TournamentViewPagerFragment implements
        PlayerItemView.OnClickListener {


    private static final String TAG = "TournamentPlayersFragment";

    private ArrayList<ListItem> mListItems;
    private boolean mInUsersRegion;
    private Player mUserPlayer;




    public static TournamentPlayersFragment create(final TournamentBundle bundle) {
        return (TournamentPlayersFragment) create(new TournamentPlayersFragment(), bundle);
    }


    @Override
    @SuppressWarnings("unchecked")
    protected TournamentAdapter createAdapter(final TournamentBundle bundle) {
        final ArrayList<Player> players = bundle.getPlayers();
        Collections.sort(players, Player.ALPHABETICAL_ORDER);
        mListItems = new ArrayList<>(players.size());

        for (final Player player : players) {
            mListItems.add(ListItem.createPlayer(player));
        }

        final AlphabeticalSectionCreator creator = new AlphabeticalSectionCreator() {
            @Override
            public AlphabeticallyComparable createDigitSection() {
                return ListItem.createTitle(getString(R.string.pound_sign));
            }


            @Override
            public AlphabeticallyComparable createLetterSection(final String letter) {
                return ListItem.createTitle(letter);
            }


            @Override
            public AlphabeticallyComparable createOtherSection() {
                return ListItem.createTitle(getString(R.string.other));
            }
        };

        mListItems = (ArrayList<ListItem>) ListUtils.createAlphabeticalList(mListItems, creator);
        return new TournamentPlayersAdapter();
    }


    @Override
    public String getFragmentName() {
        return TAG;
    }


    @Override
    public void onClick(final PlayerItemView v) {
        PlayerActivity.start(getActivity(), v.getPlayer());
    }


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInUsersRegion = User.areWeInTheUsersRegion();
        mUserPlayer = User.Player.get();
    }




    private static final class ListItem implements AlphabeticallyComparable {


        private Player mPlayer;
        private String mTitle;
        private Type mType;


        private static ListItem createPlayer(final Player player) {
            final ListItem listItem = new ListItem();
            listItem.mPlayer = player;
            listItem.mType = Type.PLAYER;

            return listItem;
        }


        private static ListItem createTitle(final String title) {
            final ListItem listItem = new ListItem();
            listItem.mTitle = title;
            listItem.mType = Type.TITLE;

            return listItem;
        }


        @Override
        public char getFirstCharOfName() {
            return toString().charAt(0);
        }


        @Override
        public String toString() {
            final String string;

            switch (mType) {
                case PLAYER:
                    string = mPlayer.getName();
                    break;

                case TITLE:
                    string = mTitle;
                    break;

                default:
                    throw new IllegalStateException("invalid Type: " + mType);
            }

            return string;
        }


        private enum Type {
            PLAYER, TITLE
        }


    }


    private final class TournamentPlayersAdapter extends TournamentAdapter {


        private static final String TAG = "TournamentPlayersAdapter";

        private final int mBgGray;
        private final int mBgHighlight;


        private TournamentPlayersAdapter() {
            mBgGray = getColorCompat(R.color.gray);
            mBgHighlight = getColorCompat(R.color.overlay_bright);
        }


        @Override
        public String getAdapterName() {
            return TAG;
        }


        @Override
        public int getItemCount() {
            return mListItems.size();
        }


        @Override
        public int getItemViewType(final int position) {
            return mListItems.get(position).mType.ordinal();
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final ListItem listItem = mListItems.get(position);

            switch (listItem.mType) {
                case PLAYER:
                    final PlayerItemView piv = ((PlayerItemView.ViewHolder) holder).getView();
                    piv.setPlayer(listItem.mPlayer);

                    if (mInUsersRegion && mUserPlayer != null) {
                        if (listItem.mPlayer.equals(mUserPlayer)) {
                            piv.setBackgroundColor(mBgHighlight);
                        } else {
                            piv.setBackgroundColor(mBgGray);
                        }
                    }
                    break;

                case TITLE:
                    ((SimpleSeparatorView.ViewHolder) holder).getView().setText(listItem.mTitle);
                    break;

                default:
                    throw new IllegalStateException("Illegal ListItem Type: " + listItem.mType);
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                final int viewType) {
            final ListItem.Type type = ListItem.Type.values()[viewType];
            final RecyclerView.ViewHolder viewHolder;

            switch (type) {
                case PLAYER: {
                    final PlayerItemView piv = PlayerItemView.inflate(parent);
                    piv.setOnClickListener(TournamentPlayersFragment.this);
                    viewHolder = piv.getViewHolder();
                    break;
                }

                case TITLE: {
                    viewHolder = SimpleSeparatorView.inflate(parent).getViewHolder();
                    break;
                }

                default:
                    throw new IllegalStateException("Illegal ListItem Type: " + type);
            }

            return viewHolder;
        }


    }


}

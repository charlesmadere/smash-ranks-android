package com.garpr.android.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Filter;

import com.garpr.android.R;
import com.garpr.android.calls.Matches;
import com.garpr.android.calls.ResponseOnUi;
import com.garpr.android.misc.Console;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.ListUtils.FilterListener;
import com.garpr.android.misc.ListUtils.SpecialFilterable;
import com.garpr.android.misc.RecyclerAdapter;
import com.garpr.android.misc.Utils;
import com.garpr.android.models.Match;
import com.garpr.android.models.Player;
import com.garpr.android.models.Region;
import com.garpr.android.models.Result;
import com.garpr.android.models.Tournament;
import com.garpr.android.settings.Settings.User;
import com.garpr.android.views.PlayerItemView;
import com.garpr.android.views.TournamentSeparatorView;

import java.util.ArrayList;
import java.util.Collections;


public class PlayerActivity extends BaseToolbarListActivity implements
        MenuItemCompat.OnActionExpandListener, PlayerItemView.OnClickListener,
        SearchView.OnQueryTextListener, TournamentSeparatorView.OnClickListener {


    private static final String CNAME = "com.garpr.android.activities.PlayerActivity";
    private static final String EXTRA_PLAYER = CNAME + ".EXTRA_PLAYER";
    private static final String KEY_MATCHES = "KEY_MATCHES";
    private static final String KEY_SHOWING = "KEY_SHOWING";
    private static final String TAG = "PlayerActivity";

    private ArrayList<ListItem> mListItems;
    private ArrayList<ListItem> mListItemsShown;
    private ArrayList<ListItem> mLoseListItems;
    private ArrayList<ListItem> mWinListItems;
    private ArrayList<Match> mMatches;
    private boolean mInUsersRegion;
    private boolean mPulled;
    private boolean mSetMenuItemsVisible;
    private Filter mFilter;
    private FilterListener<ListItem> mFilterListener;
    private Intent mShareIntent;
    private MenuItem mSearch;
    private MenuItem mShare;
    private MenuItem mShow;
    private MenuItem mShowAll;
    private MenuItem mShowLoses;
    private MenuItem mShowWins;
    private Player mPlayer;
    private Player mUserPlayer;
    private Result mShowing;




    public static void start(final Activity activity, final Player player) {
        final Intent intent = new Intent(activity, PlayerActivity.class);
        intent.putExtra(EXTRA_PLAYER, player);
        activity.startActivity(intent);
    }


    private void createListItems() {
        mListItems = new ArrayList<>();
        Tournament lastTournament = null;

        for (final Match match : mMatches) {
            final Tournament tournament = match.getTournament();

            if (!tournament.equals(lastTournament)) {
                lastTournament = tournament;
                mListItems.add(ListItem.createTournament(tournament));
            }

            mListItems.add(ListItem.createMatch(match, mPlayer));
        }

        mListItems.trimToSize();
        mListItemsShown = mListItems;

        mLoseListItems = createSortedListItems(Result.LOSE);
        mWinListItems = createSortedListItems(Result.WIN);
    }


    private ArrayList<ListItem> createSortedListItems(final Result result) {
        final ArrayList<ListItem> listItems = new ArrayList<>(mListItems.size());

        for (int i = 0; i < mListItems.size(); ++i) {
            final ListItem listItem = mListItems.get(i);

            if (listItem.isMatch() && ((listItem.mMatch.isLoser(mPlayer) && result.isLose())
                    || (listItem.mMatch.isWinner(mPlayer) && result.isWin()))) {
                ListItem tournament = null;

                for (int j = i - 1; tournament == null; --j) {
                    final ListItem li = mListItems.get(j);

                    if (li.isTournament()) {
                        tournament = li;
                    }
                }

                // make sure we haven't already added this tournament to the list
                if (!listItems.contains(tournament)) {
                    listItems.add(tournament);
                }

                listItems.add(listItem);
            }
        }

        listItems.trimToSize();
        return listItems;
    }


    private void fetchMatches() {
        setLoading(true);

        final ResponseOnUi<ArrayList<Match>> response = new ResponseOnUi<ArrayList<Match>>(TAG, this) {
            @Override
            public void errorOnUi(final Exception e) {
                mPulled = false;
                Console.e(TAG, "Exception when fetching matches", e);
                showError();
            }


            @Override
            public void successOnUi(final ArrayList<Match> list) {
                mPulled = false;
                mMatches = list;
                prepareList();
            }
        };

        Matches.get(response, mPlayer, mPulled);
    }


    @Override
    public String getActivityName() {
        return TAG;
    }


    @Override
    protected String getErrorText() {
        return getString(R.string.error_fetching_x_matches, mPlayer.getName());
    }


    @Override
    protected int getOptionsMenu() {
        return R.menu.activity_player;
    }


    private boolean isMenuNull() {
        return Utils.areAnyObjectsNull(mSearch, mShare, mShow, mShowAll, mShowLoses, mShowWins);
    }


    @Override
    public void onClick(final PlayerItemView v) {
        final Player opponent = v.getPlayer();
        HeadToHeadActivity.start(this, mPlayer, opponent);
    }


    @Override
    public void onClick(final TournamentSeparatorView v) {
        final Tournament tournament = v.getTournament();
        TournamentActivity.start(this, tournament);
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(mPlayer.getName());

        if (mPlayer.hasCompetitionValues()) {
            final Toolbar toolbar = getToolbar();
            toolbar.setSubtitle(getString(R.string.rank_x, mPlayer.getRank()));
        }

        mInUsersRegion = User.areWeInTheUsersRegion();
        mUserPlayer = User.Player.get();

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mMatches = savedInstanceState.getParcelableArrayList(KEY_MATCHES);
            mShowing = savedInstanceState.getParcelable(KEY_SHOWING);
        }

        mFilterListener = new FilterListener<ListItem>(this) {
            @Override
            public void onFilterComplete(final ArrayList<ListItem> list) {
                mListItemsShown = list;
                notifyDataSetChanged();
            }
        };

        if (mMatches == null || mMatches.isEmpty()) {
            fetchMatches();
        } else {
            prepareList();
        }
    }


    @Override
    protected void onDrawerOpened() {
        if (!isMenuNull() && MenuItemCompat.isActionViewExpanded(mSearch)) {
            MenuItemCompat.collapseActionView(mSearch);
        }
    }


    @Override
    public boolean onMenuItemActionCollapse(final MenuItem item) {
        if (Result.LOSE.equals(mShowing)) {
            mListItemsShown = mLoseListItems;
        } else if (Result.WIN.equals(mShowing)) {
            mListItemsShown = mWinListItems;
        } else {
            mListItemsShown = mListItems;
        }

        notifyDataSetChanged();
        return true;
    }


    @Override
    public boolean onMenuItemActionExpand(final MenuItem item) {
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_player_menu_share:
                share();
                break;

            case R.id.activity_player_menu_show_all:
                Utils.hideMenuItems(mShowAll);
                Utils.showMenuItems(mShowLoses, mShowWins);
                show(null);
                break;

            case R.id.activity_player_menu_show_loses:
                Utils.hideMenuItems(mShowLoses);
                Utils.showMenuItems(mShowAll, mShowWins);
                show(Result.LOSE);
                break;

            case R.id.activity_player_menu_show_wins:
                Utils.hideMenuItems(mShowWins);
                Utils.showMenuItems(mShowAll, mShowLoses);
                show(Result.WIN);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        mSearch = menu.findItem(R.id.activity_player_menu_search);
        mShare = menu.findItem(R.id.activity_player_menu_share);
        mShow = menu.findItem(R.id.activity_player_menu_show);
        mShowAll = menu.findItem(R.id.activity_player_menu_show_all);
        mShowLoses = menu.findItem(R.id.activity_player_menu_show_loses);
        mShowWins = menu.findItem(R.id.activity_player_menu_show_wins);

        MenuItemCompat.setOnActionExpandListener(mSearch, this);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearch);
        searchView.setQueryHint(getString(R.string.search_matches));
        searchView.setOnQueryTextListener(this);

        if (mSetMenuItemsVisible) {
            showMenuItems();
            mSetMenuItemsVisible = false;
        }

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onQueryTextChange(final String newText) {
        mFilter.filter(newText);
        return false;
    }


    @Override
    public boolean onQueryTextSubmit(final String query) {
        return false;
    }


    @Override
    public void onRefresh() {
        super.onRefresh();

        if (!isLoading()) {
            mPulled = false;
            MenuItemCompat.collapseActionView(mSearch);
            fetchMatches();
        }
    }


    @Override
    public void onRegionChanged(final Region region) {
        super.onRegionChanged(region);
        RankingsActivity.start(this);
    }


    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (!isMenuNull()) {
            if (mMatches != null && !mMatches.isEmpty()) {
                outState.putParcelableArrayList(KEY_MATCHES, mMatches);
            }

            if (mShowing != null) {
                outState.putParcelable(KEY_SHOWING, mShowing);
            }
        }
    }


    private void prepareList() {
        Collections.sort(mMatches, Match.REVERSE_CHRONOLOGICAL_ORDER);
        createListItems();
        setAdapter(new MatchesAdapter());

        if (mShowing != null) {
            show(mShowing);
        }
    }


    @Override
    protected void readIntentData(final Intent intent) {
        mPlayer = intent.getParcelableExtra(EXTRA_PLAYER);
    }


    @Override
    protected void setAdapter(final RecyclerAdapter adapter) {
        super.setAdapter(adapter);
        mFilter = ListUtils.createSpecialFilter(mListItems, mFilterListener);

        // it's possible for us to have gotten here before onPrepareOptionsMenu() has run

        if (isMenuNull()) {
            mSetMenuItemsVisible = true;
        } else {
            showMenuItems();
        }
    }


    private void share() {
        if (mShareIntent == null) {
            String text = null;

            if (mPlayer.hasCompetitionValues()) {
                text = getString(R.string.x_is_ranked_y_on_gar_pr_z, mPlayer.getName(),
                        mPlayer.getRank(), mPlayer.getWebUrl());
            }

            if (text == null || text.length() > Constants.TWITTER_LENGTH) {
                text = getString(R.string.x_on_gar_pr_y, mPlayer.getName(), mPlayer.getWebUrl());
            }

            if (text.length() > Constants.TWITTER_LENGTH) {
                text = getString(R.string.gar_pr_x, mPlayer.getWebUrl());
            }

            if (text.length() > Constants.TWITTER_LENGTH) {
                text = mPlayer.getWebUrl();
            }

            final String title = getString(R.string.x_on_gar_pr, mPlayer.getName());
            mShareIntent = new Intent(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, text)
                    .putExtra(Intent.EXTRA_TITLE, title)
                    .setType(Constants.MIMETYPE_TEXT_PLAIN);

            mShareIntent = Intent.createChooser(mShareIntent, getString(R.string.share_to));
        }

        startActivity(mShareIntent);
    }


    private void show(final Result result) {
        mShowing = result;

        if (Result.LOSE.equals(result)) {
            mListItemsShown = mLoseListItems;
        } else if (Result.WIN.equals(result)) {
            mListItemsShown = mWinListItems;
        } else {
            mListItemsShown = mListItems;
        }

        notifyDataSetChanged();
        mFilter = ListUtils.createSpecialFilter(mListItemsShown, mFilterListener);
    }


    @Override
    protected boolean showDrawerIndicator() {
        return false;
    }


    private void showMenuItems() {
        Utils.showMenuItems(mSearch, mShare, mShow);

        if (Result.LOSE.equals(mShowing)) {
            Utils.hideMenuItems(mShowLoses);
            Utils.showMenuItems(mShowAll, mShowWins);
        } else if (Result.WIN.equals(mShowing)) {
            Utils.hideMenuItems(mShowWins);
            Utils.showMenuItems(mShowAll, mShowLoses);
        } else {
            Utils.hideMenuItems(mShowAll);
            Utils.showMenuItems(mShowLoses, mShowWins);
        }
    }




    private static final class ListItem implements SpecialFilterable {


        private static long sId;

        private long mId;
        private Match mMatch;
        private Player mOpponent;
        private Tournament mTournament;
        private Type mType;


        private static ListItem createMatch(final Match match, final Player player) {
            final ListItem item = new ListItem();
            item.mId = sId++;
            item.mMatch = match;
            item.mOpponent = match.getOtherPlayer(player);
            item.mType = Type.MATCH;

            return item;
        }


        private static ListItem createTournament(final Tournament tournament) {
            final ListItem item = new ListItem();
            item.mId = sId++;
            item.mTournament = tournament;
            item.mType = Type.TOURNAMENT;

            return item;
        }


        @Override
        public boolean equals(final Object o) {
            final boolean isEqual;

            if (this == o) {
                isEqual = true;
            } else if (o instanceof ListItem) {
                final ListItem li = (ListItem) o;

                if (isMatch() && li.isMatch()) {
                    isEqual = mMatch.equals(li.mMatch);
                } else if (isTournament() && li.isTournament()) {
                    isEqual = mTournament.equals(li.mTournament);
                } else {
                    isEqual = false;
                }
            } else {
                isEqual = false;
            }

            return isEqual;
        }


        @Override
        public String getName() {
            final String name;

            switch (mType) {
                case MATCH:
                    name = mOpponent.getName();
                    break;

                case TOURNAMENT:
                    name = mTournament.getName();
                    break;

                default:
                    throw new IllegalStateException("invalid ListItem Type: " + mType);
            }

            return name;
        }


        @Override
        public boolean isBasicItem() {
            return isMatch();
        }


        private boolean isMatch() {
            return mType.equals(Type.MATCH);
        }


        private boolean isTournament() {
            return mType.equals(Type.TOURNAMENT);
        }


        @Override
        public boolean isSpecialItem() {
            return isTournament();
        }


        @Override
        public String toString() {
            return getName();
        }


        private enum Type {
            MATCH, TOURNAMENT
        }


    }


    private final class MatchesAdapter extends RecyclerAdapter {


        private static final String TAG = "MatchesAdapter";

        private final int mBgGray;
        private final int mBgHighlight;
        private final int mColorLose;
        private final int mColorWin;


        private MatchesAdapter() {
            super(getRecyclerView());

            mBgGray = getColorCompat(R.color.gray);
            mBgHighlight = getColorCompat(R.color.overlay_bright);
            mColorLose = getColorCompat(R.color.lose_pink);
            mColorWin = getColorCompat(R.color.win_green);
        }


        @Override
        public String getAdapterName() {
            return TAG;
        }


        @Override
        public int getItemCount() {
            return mListItemsShown.size();
        }


        @Override
        public long getItemId(final int position) {
            return mListItemsShown.get(position).mId;
        }


        @Override
        public int getItemViewType(final int position) {
            return mListItemsShown.get(position).mType.ordinal();
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final ListItem listItem = mListItemsShown.get(position);

            switch (listItem.mType) {
                case MATCH:
                    final PlayerItemView piv = ((PlayerItemView.ViewHolder) holder).getView();
                    piv.setPlayer(listItem.mOpponent);

                    if (listItem.mMatch.isWinner(mPlayer)) {
                        piv.getNameView().setTextColor(mColorWin);
                    } else {
                        piv.getNameView().setTextColor(mColorLose);
                    }

                    if (mInUsersRegion && mUserPlayer != null) {
                        if (listItem.mOpponent.equals(mUserPlayer)) {
                            piv.setBackgroundColor(mBgHighlight);
                        } else {
                            piv.setBackgroundColor(mBgGray);
                        }
                    }
                    break;

                case TOURNAMENT:
                    ((TournamentSeparatorView.ViewHolder) holder).getView().setTournament(
                            listItem.mTournament);
                    break;

                default:
                    throw new RuntimeException("Unknown ListItem Type: " + listItem.mType);
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                final int viewType) {
            final ListItem.Type listItemType = ListItem.Type.values()[viewType];
            final RecyclerView.ViewHolder holder;

            switch (listItemType) {
                case MATCH:
                    final PlayerItemView piv = PlayerItemView.inflate(parent);
                    piv.setOnClickListener(PlayerActivity.this);
                    holder = piv.getViewHolder();
                    break;

                case TOURNAMENT:
                    final TournamentSeparatorView tsv = TournamentSeparatorView.inflate(parent);
                    tsv.setOnClickListener(PlayerActivity.this);
                    holder = tsv.getViewHolder();
                    break;

                default:
                    throw new RuntimeException("Unknown ListItem Type: " + listItemType);
            }

            return holder;
        }


    }


}

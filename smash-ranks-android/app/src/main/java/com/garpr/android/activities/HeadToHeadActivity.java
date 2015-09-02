package com.garpr.android.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.calls.Matches;
import com.garpr.android.calls.ResponseOnUi;
import com.garpr.android.misc.Console;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.ListUtils.MonthlyComparable;
import com.garpr.android.misc.ListUtils.MonthlySectionCreator;
import com.garpr.android.misc.RecyclerAdapter;
import com.garpr.android.misc.Utils;
import com.garpr.android.models.BaseDateWrapper;
import com.garpr.android.models.HeadToHeadBundle;
import com.garpr.android.models.Match;
import com.garpr.android.models.Player;
import com.garpr.android.models.Result;
import com.garpr.android.models.Tournament;
import com.garpr.android.views.MatchResultsItemView;
import com.garpr.android.views.SimpleSeparatorView;
import com.garpr.android.views.TournamentItemView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class HeadToHeadActivity extends BaseToolbarListActivity implements
        TournamentItemView.OnClickListener {


    private static final String CNAME = "com.garpr.android.activities.HeadToHeadActivity";
    private static final String EXTRA_PLAYER = CNAME + ".EXTRA_PLAYER";
    private static final String EXTRA_OPPONENT = CNAME + ".EXTRA_OPPONENT";
    private static final String KEY_BUNDLE = "KEY_BUNDLE";
    private static final String KEY_SHOWING = "KEY_SHOWING";
    private static final String TAG = "HeadToHeadActivity";

    private ArrayList<ListItem> mListItems;
    private ArrayList<ListItem> mListItemsShown;
    private ArrayList<ListItem> mLoseListItems;
    private ArrayList<ListItem> mWinListItems;
    private boolean mPulled;
    private boolean mSetMenuItemsVisible;
    private HeadToHeadBundle mBundle;
    private MenuItem mShow;
    private MenuItem mShowAll;
    private MenuItem mShowLoses;
    private MenuItem mShowWins;
    private Player mOpponent;
    private Player mPlayer;
    private Result mShowing;




    public static void start(final Activity activity, final Player player, final Player opponent) {
        final Intent intent = new Intent(activity, HeadToHeadActivity.class);
        intent.putExtra(EXTRA_PLAYER, player);
        intent.putExtra(EXTRA_OPPONENT, opponent);
        activity.startActivity(intent);
    }


    @SuppressWarnings("unchecked")
    private void createListItems() {
        final ArrayList<Match> matches = mBundle.getMatches();
        Collections.sort(matches, Match.REVERSE_CHRONOLOGICAL_ORDER);
        mListItems = new ArrayList<>();

        for (final Match match : matches) {
            mListItems.add(ListItem.createTournament(match));
        }

        final MonthlySectionCreator creator = new MonthlySectionCreator() {
            @Override
            public MonthlyComparable createMonthlySection(final BaseDateWrapper dateWrapper) {
                return ListItem.createDate(dateWrapper);
            }
        };

        mListItems = (ArrayList<ListItem>) ListUtils.createMonthlyList(mListItems, creator);
        mListItems.add(0, ListItem.createResults(mBundle.getLosses(), mBundle.getWins()));
        mListItems.trimToSize();
        mListItemsShown = mListItems;

        mLoseListItems = createSortedListItems(Result.LOSE);
        mWinListItems = createSortedListItems(Result.WIN);
    }


    private ArrayList<ListItem> createSortedListItems(final Result result) {
        final ArrayList<ListItem> listItems = new ArrayList<>(mListItems.size());

        for (int i = 0; i < mListItems.size(); ++i) {
            final ListItem listItem = mListItems.get(i);

            if (listItem.isResults()) {
                listItems.add(listItem);
            } else if (listItem.isTournament() && ((listItem.mMatch.isLoser(mPlayer) &&
                    result.isLose()) || (listItem.mMatch.isWinner(mPlayer) && result.isWin()))) {
                ListItem date = null;

                for (int j = i - 1; date == null; --j) {
                    final ListItem li = mListItems.get(j);

                    if (li.isDate()) {
                        date = li;
                    }
                }

                // make sure we haven't already added this date to the list
                if (!listItems.contains(date)) {
                    listItems.add(date);
                }

                listItems.add(listItem);
            }
        }

        listItems.trimToSize();
        return listItems;
    }


    private void fetchMatches() {
        setLoading(true);

        final ResponseOnUi<HeadToHeadBundle> response = new ResponseOnUi<HeadToHeadBundle>(TAG, this) {
            @Override
            public void errorOnUi(final Exception e) {
                mPulled = false;
                Console.e(TAG, "Exception when fetching head to head matches", e);
                showError();
            }


            @Override
            public void successOnUi(final HeadToHeadBundle headToHeadBundle) {
                mPulled = false;
                mBundle = headToHeadBundle;
                prepareList();
            }
        };

        Matches.getHeadToHead(response, mPlayer, mOpponent, mPulled);
    }


    @Override
    public String getActivityName() {
        return TAG;
    }


    @Override
    protected String getErrorText() {
        return getString(R.string.error_fetching_head_to_head_matches);
    }


    @Override
    protected int getOptionsMenu() {
        return R.menu.activity_head_to_head;
    }


    private boolean isMenuNull() {
        return Utils.areAnyObjectsNull(mShow, mShowAll, mShowLoses, mShowWins);
    }


    @Override
    public void onClick(final TournamentItemView v) {
        final Tournament tournament = v.getTournament();
        TournamentActivity.start(this, tournament);
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.x_vs_y, mPlayer.getName(), mOpponent.getName()));

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mBundle = savedInstanceState.getParcelable(KEY_BUNDLE);
            mShowing = savedInstanceState.getParcelable(KEY_SHOWING);
        }

        if (mBundle == null) {
            fetchMatches();
        } else {
            prepareList();
        }
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_head_to_head_menu_show_all:
                Utils.hideMenuItems(mShowAll);
                Utils.showMenuItems(mShowLoses, mShowWins);
                show(null);
                break;

            case R.id.activity_head_to_head_menu_show_loses:
                Utils.hideMenuItems(mShowLoses);
                Utils.showMenuItems(mShowAll, mShowWins);
                show(Result.LOSE);
                break;

            case R.id.activity_head_to_head_menu_show_wins:
                Utils.hideMenuItems(mShowWins);
                Utils.showMenuItems(mShowAll, mShowLoses);
                show(Result.WIN);
                break;

            case R.id.activity_head_to_head_menu_view_player_one:
                PlayerActivity.start(this, mPlayer);
                break;

            case R.id.activity_head_to_head_menu_view_player_two:
                PlayerActivity.start(this, mOpponent);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        mShow = menu.findItem(R.id.activity_head_to_head_menu_show);
        mShowAll = menu.findItem(R.id.activity_head_to_head_menu_show_all);
        mShowLoses = menu.findItem(R.id.activity_head_to_head_menu_show_loses);
        mShowWins = menu.findItem(R.id.activity_head_to_head_menu_show_wins);

        if (mSetMenuItemsVisible) {
            showMenuItems();
            mSetMenuItemsVisible = false;
        }

        // the below menu items are separate from the above (there is no need to ever
        // programmatically hide / show them and such)

        final MenuItem player = menu.findItem(R.id.activity_head_to_head_menu_view_player_one);
        player.setTitle(getString(R.string.view_x, mPlayer.getName()));

        final MenuItem opponent = menu.findItem(R.id.activity_head_to_head_menu_view_player_two);
        opponent.setTitle(getString(R.string.view_x, mOpponent.getName()));

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onRefresh() {
        super.onRefresh();

        if (!isLoading()) {
            mPulled = true;
            fetchMatches();
        }
    }


    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (!isMenuNull()) {
            if (mBundle != null) {
                outState.putParcelable(KEY_BUNDLE, mBundle);
            }

            if (mShowing != null) {
                outState.putParcelable(KEY_SHOWING, mShowing);
            }
        }
    }


    private void prepareList() {
        createListItems();
        setAdapter(new MatchesAdapter());

        if (mShowing != null) {
            show(mShowing);
        }
    }


    @Override
    protected void readIntentData(final Intent intent) {
        mOpponent = intent.getParcelableExtra(EXTRA_OPPONENT);
        mPlayer = intent.getParcelableExtra(EXTRA_PLAYER);
    }


    @Override
    protected void setAdapter(final RecyclerAdapter adapter) {
        super.setAdapter(adapter);

        // it's possible for us to have gotten here before onPrepareOptionsMenu() has run

        if (isMenuNull()) {
            mSetMenuItemsVisible = true;
        } else {
            showMenuItems();
        }
    }


    @Override
    protected boolean showDrawerIndicator() {
        return false;
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
    }


    private void showMenuItems() {
        Utils.showMenuItems(mShow);

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




    private static final class ListItem implements MonthlyComparable {


        private BaseDateWrapper mDateWrapper;
        private int[] mResults;
        private Match mMatch;
        private Type mType;


        private static ListItem createDate(final BaseDateWrapper dateWrapper) {
            final ListItem listItem = new ListItem();
            listItem.mDateWrapper = dateWrapper;
            listItem.mType = Type.DATE;

            return listItem;
        }


        private static ListItem createResults(final int loses, final int wins) {
            final ListItem listItem = new ListItem();
            listItem.mResults = new int[] { loses, wins };
            listItem.mType = Type.RESULTS;

            return listItem;
        }


        private static ListItem createTournament(final Match match) {
            final ListItem listItem = new ListItem();
            listItem.mDateWrapper = match.getDateWrapper();
            listItem.mMatch = match;
            listItem.mType = Type.TOURNAMENT;

            return listItem;
        }


        @Override
        public boolean equals(final Object o) {
            final boolean isEqual;

            if (this == o) {
                isEqual = true;
            } else if (o instanceof ListItem) {
                final ListItem li = (ListItem) o;

                if (isDate() && li.isDate()) {
                    isEqual = mDateWrapper.equals(li.mDateWrapper);
                } else if (isResults() && li.isResults()) {
                    isEqual = Arrays.equals(mResults, li.mResults);
                } else if (isTournament() && li.isTournament()) {
                    isEqual = mMatch.equals(li.mMatch);
                } else {
                    isEqual = false;
                }
            } else {
                isEqual = false;
            }

            return isEqual;
        }


        @Override
        public BaseDateWrapper getDateWrapper() {
            return mDateWrapper;
        }


        private boolean isDate() {
            return mType.equals(Type.DATE);
        }


        private boolean isResults() {
            return mType.equals(Type.RESULTS);
        }


        private boolean isTournament() {
            return mType.equals(Type.TOURNAMENT);
        }


        @Override
        public String toString() {
            final String title;

            switch (mType) {
                case DATE:
                    title = mDateWrapper.getMonthAndYear();
                    break;

                case RESULTS:
                    final Context context = App.getContext();
                    title = context.getString(R.string.x_em_dash_y, mResults[0], mResults[1]);
                    break;

                case TOURNAMENT:
                    title = mMatch.getTournament().getName();
                    break;

                default:
                    throw new IllegalStateException("invalid ListItem Type: " + mType);
            }

            return title;
        }


        private enum Type {
            DATE, RESULTS, TOURNAMENT
        }


    }


    private final class MatchesAdapter extends RecyclerAdapter {


        private static final String TAG = "MatchesAdapter";

        private final int mColorLose;
        private final int mColorWin;


        private MatchesAdapter() {
            super(getRecyclerView());

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
        public int getItemViewType(final int position) {
            return mListItemsShown.get(position).mType.ordinal();
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final ListItem listItem = mListItemsShown.get(position);

            switch (listItem.mType) {
                case DATE:
                    ((SimpleSeparatorView.ViewHolder) holder).getView().setText(
                            listItem.mDateWrapper.getMonthAndYear());
                    break;

                case RESULTS:
                    ((MatchResultsItemView.ViewHolder) holder).getView().setResults(listItem.mResults);
                    break;

                case TOURNAMENT:
                    final TournamentItemView tiv = ((TournamentItemView.ViewHolder) holder).getView();
                    tiv.setTournament(listItem.mMatch.getTournament());

                    if (listItem.mMatch.isWinner(mPlayer)) {
                        tiv.getNameView().setTextColor(mColorWin);
                    } else {
                        tiv.getNameView().setTextColor(mColorLose);
                    }
                    break;

                default:
                    throw new RuntimeException("Unknown ListItem Type: " + listItem.mType);
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                final int viewType) {
            final ListItem.Type type = ListItem.Type.values()[viewType];
            final RecyclerView.ViewHolder holder;

            switch (type) {
                case DATE:
                    holder = SimpleSeparatorView.inflate(parent).getViewHolder();
                    break;

                case RESULTS:
                    holder = MatchResultsItemView.inflate(parent).getViewHolder();
                    break;

                case TOURNAMENT:
                    final TournamentItemView tiv = TournamentItemView.inflate(parent);
                    tiv.setOnClickListener(HeadToHeadActivity.this);
                    holder = tiv.getViewHolder();
                    break;

                default:
                    throw new RuntimeException("Unknown ListItem Type: " + type);
            }

            return holder;
        }


    }


}

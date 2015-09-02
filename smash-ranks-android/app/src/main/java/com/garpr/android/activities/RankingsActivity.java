package com.garpr.android.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Toast;

import com.garpr.android.R;
import com.garpr.android.calls.Rankings;
import com.garpr.android.calls.ResponseOnUi;
import com.garpr.android.misc.Console;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.ListUtils.AlphabeticallyComparable;
import com.garpr.android.misc.ListUtils.SpecialFilterable;
import com.garpr.android.misc.NetworkCache;
import com.garpr.android.misc.RecyclerAdapter;
import com.garpr.android.misc.SyncManager;
import com.garpr.android.misc.Utils;
import com.garpr.android.models.Player;
import com.garpr.android.models.RankingsBundle;
import com.garpr.android.models.Region;
import com.garpr.android.settings.Settings;
import com.garpr.android.settings.Settings.User;
import com.garpr.android.views.RankingItemView;
import com.garpr.android.views.SimpleSeparatorView;

import java.util.ArrayList;
import java.util.Collections;


public class RankingsActivity extends BaseToolbarListActivity implements
        MenuItemCompat.OnActionExpandListener, RankingItemView.OnClickListener,
        SearchView.OnQueryTextListener {


    private static final String EXTRA_IS_FROM_RANKINGS_UPDATE = "EXTRA_IS_FROM_RANKINGS_UPDATE";
    private static final String KEY_PLAYERS = "KEY_PLAYERS";
    private static final String KEY_RANKINGS_DATE = "KEY_RANKINGS_DATE";
    private static final String TAG = "RankingsActivity";

    private ArrayList<ListItem> mListItems;
    private ArrayList<ListItem> mListItemsShown;
    private ArrayList<Player> mPlayers;
    private boolean mInUsersRegion;
    private boolean mPulled;
    private boolean mSetMenuItemsVisible;
    private CharSequence mRankingsDate;
    private Filter mFilter;
    private MenuItem mDate;
    private MenuItem mSearch;
    private Player mUserPlayer;




    public static void start(final Activity activity) {
        activity.startActivity(new IntentBuilder(activity).build());
    }


    private void createListItems() {
        mListItems = new ArrayList<>();

        final Resources resources = getResources();
        final int ranksPerSection = resources.getInteger(R.integer.ranks_per_section);
        final int playersSize = mPlayers.size();

        for (int i = 0; i < playersSize; ++i) {
            final Player player = mPlayers.get(i);

            String listItemTitle = null;

            if (i % ranksPerSection == 0) {
                final int sectionStart = player.getRank();
                final int sectionEnd;

                if (sectionStart + ranksPerSection - 1 > playersSize) {
                    sectionEnd = playersSize;
                } else {
                    sectionEnd = sectionStart + ranksPerSection - 1;
                }

                listItemTitle = getString(R.string.x_em_dash_y, sectionStart, sectionEnd);
            }

            if (listItemTitle != null) {
                final ListItem listItem = ListItem.createTitle(listItemTitle);
                mListItems.add(listItem);
            }

            final ListItem listItem = ListItem.createPlayer(player);
            mListItems.add(listItem);
        }

        mListItems.trimToSize();
        mListItemsShown = mListItems;
    }


    private void fetchRankings() {
        setLoading(true);

        final ResponseOnUi<RankingsBundle> response = new ResponseOnUi<RankingsBundle>(TAG, this) {
            @Override
            public void errorOnUi(final Exception e) {
                mPulled = false;
                Console.e(TAG, "Exception when retrieving rankings", e);
                showError();
            }


            @Override
            public void successOnUi(final RankingsBundle rankingsBundle) {
                mPulled = false;
                mPlayers = rankingsBundle.getRankings();

                if (rankingsBundle.hasDateWrapper()) {
                    mRankingsDate = DateUtils.getRelativeDateTimeString(RankingsActivity.this,
                            rankingsBundle.getDateWrapper().getDate().getTime(),
                            DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0);
                }

                prepareList();
            }
        };

        Rankings.get(response, mPulled);
    }


    @Override
    public String getActivityName() {
        return TAG;
    }


    @Override
    protected String getErrorText() {
        return getString(R.string.error_fetching_rankings);
    }


    @Override
    protected int getOptionsMenu() {
        return R.menu.activity_rankings;
    }


    @Override
    protected int getSelectedNavigationItemId() {
        return R.id.navigation_view_menu_rankings;
    }


    private boolean isMenuNull() {
        return Utils.areAnyObjectsNull(mDate, mSearch);
    }


    @Override
    public void onClick(final RankingItemView v) {
        final Player player = v.getPlayer();
        PlayerActivity.start(this, player);
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra(EXTRA_IS_FROM_RANKINGS_UPDATE, false)) {
            intent.putExtra(EXTRA_IS_FROM_RANKINGS_UPDATE, false);
            Settings.Region.set(User.Region.get());
            NetworkCache.clear();
        }

        mInUsersRegion = User.areWeInTheUsersRegion();
        mUserPlayer = User.Player.get();

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mPlayers = savedInstanceState.getParcelableArrayList(KEY_PLAYERS);
            mRankingsDate = savedInstanceState.getCharSequence(KEY_RANKINGS_DATE);
        }

        if (mPlayers == null || mPlayers.isEmpty()) {
            fetchRankings();
        } else {
            prepareList();
        }

        if (Settings.Sync.IsEnabled.get() && !Settings.Sync.IsScheduled.get()) {
            SyncManager.schedule();
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
        mListItemsShown = mListItems;
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
            case R.id.activity_rankings_menu_date:
                Toast.makeText(this, getString(R.string.updated_x, mRankingsDate),
                        Toast.LENGTH_LONG).show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        mDate = menu.findItem(R.id.activity_rankings_menu_date);
        mSearch = menu.findItem(R.id.activity_rankings_menu_search);

        MenuItemCompat.setOnActionExpandListener(mSearch, this);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearch);
        searchView.setQueryHint(getString(R.string.search_players));
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
            mPulled = true;
            MenuItemCompat.collapseActionView(mSearch);
            fetchRankings();
        }
    }


    @Override
    public void onRegionChanged(final Region region) {
        super.onRegionChanged(region);
        mInUsersRegion = User.areWeInTheUsersRegion();
        fetchRankings();
    }


    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mPlayers != null && !mPlayers.isEmpty()) {
            outState.putParcelableArrayList(KEY_PLAYERS, mPlayers);
            outState.putCharSequence(KEY_RANKINGS_DATE, mRankingsDate);
        }
    }


    private void prepareList() {
        Collections.sort(mPlayers, Player.RANK_ORDER);
        createListItems();
        setAdapter(new RankingsAdapter());
    }


    @Override
    protected void setAdapter(final RecyclerAdapter adapter) {
        super.setAdapter(adapter);

        final ListUtils.FilterListener<ListItem> listener = new ListUtils.FilterListener<ListItem>(this) {
            @Override
            public void onFilterComplete(final ArrayList<ListItem> list) {
                mListItemsShown = list;
                notifyDataSetChanged();
            }
        };

        mFilter = ListUtils.createSpecialFilter(mListItems, listener);

        // it's possible for us to have gotten here before onPrepareOptionsMenu() has run

        if (isMenuNull()) {
            mSetMenuItemsVisible = true;
        } else {
            showMenuItems();
        }
    }


    private void showMenuItems() {
        Utils.showMenuItems(mDate, mSearch);
    }




    public static final class IntentBuilder extends BaseActivity.IntentBuilder {


        public IntentBuilder(final Context context) {
            super(context, RankingsActivity.class);
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }


        public IntentBuilder isFromRankingsUpdate() {
            mIntent.putExtra(EXTRA_IS_FROM_RANKINGS_UPDATE, true);
            return this;
        }


    }


    private static final class ListItem implements AlphabeticallyComparable, SpecialFilterable {


        private static long sId;

        private long mId;
        private Player mPlayer;
        private String mTitle;
        private Type mType;


        private static ListItem createPlayer(final Player player) {
            final ListItem item = new ListItem();
            item.mId = sId++;
            item.mPlayer = player;
            item.mType = Type.PLAYER;

            return item;
        }


        private static ListItem createTitle(final String title) {
            final ListItem item = new ListItem();
            item.mId = sId++;
            item.mTitle = title;
            item.mType = Type.TITLE;

            return item;
        }


        @Override
        public boolean equals(final Object o) {
            final boolean isEqual;

            if (this == o) {
                isEqual = true;
            } else if (o instanceof ListItem) {
                final ListItem li = (ListItem) o;

                if (isPlayer() && li.isPlayer()) {
                    isEqual = mPlayer.equals(li.mPlayer);
                } else if (isTitle() && li.isTitle()) {
                    isEqual = mTitle.equals(li.mTitle);
                } else {
                    isEqual = false;
                }
            } else {
                isEqual = false;
            }

            return isEqual;
        }


        @Override
        public char getFirstCharOfName() {
            return getName().charAt(0);
        }


        @Override
        public String getName() {
            final String name;

            switch (mType) {
                case PLAYER:
                    name = mPlayer.getName();
                    break;

                case TITLE:
                    name = mTitle;
                    break;

                default:
                    throw new IllegalStateException("invalid ListItem Type: " + mType);
            }

            return name;
        }


        @Override
        public boolean isBasicItem() {
            return isPlayer();
        }


        private boolean isPlayer() {
            return mType.equals(Type.PLAYER);
        }


        @Override
        public boolean isSpecialItem() {
            return isTitle();
        }


        private boolean isTitle() {
            return mType.equals(Type.TITLE);
        }


        @Override
        public String toString() {
            return getName();
        }


        private enum Type {
            PLAYER, TITLE
        }


    }


    private final class RankingsAdapter extends RecyclerAdapter {


        private static final String TAG = "RankingsAdapter";

        private final int mBgGray;
        private final int mBgHighlight;


        private RankingsAdapter() {
            super(getRecyclerView());

            mBgGray = getColorCompat(R.color.gray);
            mBgHighlight = getColorCompat(R.color.overlay_bright);
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
                case PLAYER:
                    final RankingItemView riv = ((RankingItemView.ViewHolder) holder).getView();
                    riv.setPlayer(listItem.mPlayer);

                    if (mInUsersRegion && mUserPlayer != null) {
                        if (listItem.mPlayer.equals(mUserPlayer)) {
                            riv.setBackgroundColor(mBgHighlight);
                        } else {
                            riv.setBackgroundColor(mBgGray);
                        }
                    }
                    break;

                case TITLE: {
                    ((SimpleSeparatorView.ViewHolder) holder).getView().setText(listItem.mTitle);
                    break;
                }

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
                case PLAYER:
                    final RankingItemView riv = RankingItemView.inflate(parent);
                    riv.setOnClickListener(RankingsActivity.this);
                    holder = riv.getViewHolder();
                    break;

                case TITLE:
                    holder = SimpleSeparatorView.inflate(parent)
                            .getViewHolder();
                    break;

                default:
                    throw new RuntimeException("Unknown ListItem Type: " + listItemType);
            }

            return holder;
        }


    }


}

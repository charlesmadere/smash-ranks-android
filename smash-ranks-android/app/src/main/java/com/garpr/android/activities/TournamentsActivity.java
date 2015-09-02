package com.garpr.android.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Filter;

import com.garpr.android.R;
import com.garpr.android.calls.ResponseOnUi;
import com.garpr.android.calls.Tournaments;
import com.garpr.android.misc.Console;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.ListUtils.MonthlyComparable;
import com.garpr.android.misc.ListUtils.MonthlySectionCreator;
import com.garpr.android.misc.ListUtils.SpecialFilterable;
import com.garpr.android.misc.RecyclerAdapter;
import com.garpr.android.misc.Utils;
import com.garpr.android.models.BaseDateWrapper;
import com.garpr.android.models.Region;
import com.garpr.android.models.Tournament;
import com.garpr.android.views.SimpleSeparatorView;
import com.garpr.android.views.TournamentItemView;

import java.util.ArrayList;
import java.util.Collections;


public class TournamentsActivity extends BaseToolbarListActivity implements
        MenuItemCompat.OnActionExpandListener, SearchView.OnQueryTextListener,
        TournamentItemView.OnClickListener {


    private static final String KEY_TOURNAMENTS = "KEY_TOURNAMENTS";
    private static final String TAG = "TournamentsActivity";

    private ArrayList<ListItem> mListItems;
    private ArrayList<ListItem> mListItemsShown;
    private ArrayList<Tournament> mTournaments;
    private boolean mPulled;
    private boolean mSetMenuItemsVisible;
    private Filter mFilter;
    private MenuItem mSearch;




    public static void start(final Activity activity) {
        final Intent intent = new Intent(activity, TournamentsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
    }


    @SuppressWarnings("unchecked")
    private void createListItems() {
        mListItems = new ArrayList<>();

        for (final Tournament tournament : mTournaments) {
            mListItems.add(ListItem.createTournament(tournament));
        }

        final MonthlySectionCreator creator = new MonthlySectionCreator() {
            @Override
            public MonthlyComparable createMonthlySection(final BaseDateWrapper dateWrapper) {
                return ListItem.createDate(dateWrapper);
            }
        };

        mListItems = (ArrayList<ListItem>) ListUtils.createMonthlyList(mListItems, creator);
        mListItems.trimToSize();
        mListItemsShown = mListItems;
    }


    private void fetchTournaments() {
        setLoading(true);

        final ResponseOnUi<ArrayList<Tournament>> response = new ResponseOnUi<ArrayList<Tournament>>(TAG, this) {
            @Override
            public void errorOnUi(final Exception e) {
                mPulled = false;
                Console.e(TAG, "Exception when retrieving tournaments", e);
                showError();
            }


            @Override
            public void successOnUi(final ArrayList<Tournament> list) {
                mPulled = false;
                mTournaments = list;
                prepareList();
            }
        };

        Tournaments.getAll(response, mPulled);
    }


    @Override
    public String getActivityName() {
        return TAG;
    }


    @Override
    protected String getErrorText() {
        return getString(R.string.error_fetching_tournaments);
    }


    @Override
    protected int getOptionsMenu() {
        return R.menu.activity_tournaments;
    }


    @Override
    protected int getSelectedNavigationItemId() {
        return R.id.navigation_view_menu_tournaments;
    }


    private boolean isMenuNull() {
        return Utils.areAnyObjectsNull(mSearch);
    }


    @Override
    public void onClick(final TournamentItemView v) {
        final Tournament tournament = v.getTournament();
        TournamentActivity.start(this, tournament);
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mTournaments = savedInstanceState.getParcelableArrayList(KEY_TOURNAMENTS);
        }

        if (mTournaments == null || mTournaments.isEmpty()) {
            fetchTournaments();
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
        mListItemsShown = mListItems;
        notifyDataSetChanged();
        return true;
    }


    @Override
    public boolean onMenuItemActionExpand(final MenuItem item) {
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        mSearch = menu.findItem(R.id.activity_tournaments_menu_search);

        MenuItemCompat.setOnActionExpandListener(mSearch, this);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearch);
        searchView.setQueryHint(getString(R.string.search_tournaments));
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
            fetchTournaments();
        }
    }


    @Override
    public void onRegionChanged(final Region region) {
        super.onRegionChanged(region);
        fetchTournaments();
    }


    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mTournaments != null && !mTournaments.isEmpty()) {
            outState.putParcelableArrayList(KEY_TOURNAMENTS, mTournaments);
        }
    }


    private void prepareList() {
        Collections.sort(mTournaments, Tournament.REVERSE_CHRONOLOGICAL_ORDER);
        createListItems();
        setAdapter(new TournamentsAdapter());
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
        Utils.showMenuItems(mSearch);
    }




    private final static class ListItem implements MonthlyComparable, SpecialFilterable {


        private static long sId;

        private BaseDateWrapper mDateWrapper;
        private long mId;
        private Tournament mTournament;
        private Type mType;


        private static ListItem createDate(final BaseDateWrapper dateWrapper) {
            final ListItem item = new ListItem();
            item.mDateWrapper = dateWrapper;
            item.mId = sId++;
            item.mType = Type.DATE;

            return item;
        }


        private static ListItem createTournament(final Tournament tournament) {
            final ListItem item = new ListItem();
            item.mDateWrapper = tournament.getDateWrapper();
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

                if (isDate() && li.isDate()) {
                    isEqual = mDateWrapper.equals(li.mDateWrapper);
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
        public BaseDateWrapper getDateWrapper() {
            return mDateWrapper;
        }


        @Override
        public String getName() {
            final String name;

            switch (mType) {
                case DATE:
                    name = mDateWrapper.getMonthAndYear();
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
            return isTournament();
        }


        private boolean isDate() {
            return mType.equals(Type.DATE);
        }


        @Override
        public boolean isSpecialItem() {
            return isDate();
        }


        private boolean isTournament() {
            return mType.equals(Type.TOURNAMENT);
        }


        @Override
        public String toString() {
            return getName();
        }


        private enum Type {
            DATE, TOURNAMENT
        }


    }


    private final class TournamentsAdapter extends RecyclerAdapter {


        private static final String TAG = "TournamentsAdapter";


        private TournamentsAdapter() {
            super(getRecyclerView());
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
                case DATE:
                    ((SimpleSeparatorView.ViewHolder) holder).getView().setText(
                            listItem.mDateWrapper.getMonthAndYear());
                    break;

                case TOURNAMENT:
                    final TournamentItemView tiv = ((TournamentItemView.ViewHolder) holder).getView();
                    tiv.setTournament(listItem.mTournament);
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
                case DATE:
                    holder = SimpleSeparatorView.inflate(parent).getViewHolder();
                    break;

                case TOURNAMENT:
                    final TournamentItemView tiv = TournamentItemView.inflate(parent);
                    tiv.setOnClickListener(TournamentsActivity.this);
                    holder = tiv.getViewHolder();
                    break;

                default:
                    throw new RuntimeException("Unknown ListItem Type: " + listItemType);
            }

            return holder;
        }


    }


}

package com.garpr.android.fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.calls.Players;
import com.garpr.android.calls.ResponseOnUi;
import com.garpr.android.misc.Console;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.ListUtils.AlphabeticalSectionCreator;
import com.garpr.android.misc.ListUtils.AlphabeticallyComparable;
import com.garpr.android.misc.ListUtils.SpecialFilterable;
import com.garpr.android.misc.RecyclerAdapter;
import com.garpr.android.misc.Utils;
import com.garpr.android.models.Player;
import com.garpr.android.views.CheckableItemView;
import com.garpr.android.views.SimpleSeparatorView;

import java.util.ArrayList;
import java.util.Collections;


public class PlayersFragment extends BaseListToolbarFragment implements
        CheckableItemView.OnClickListener, MenuItemCompat.OnActionExpandListener,
        SearchView.OnQueryTextListener {


    private static final String KEY_PLAYERS = "KEY_PLAYERS";
    private static final String KEY_SELECTED_PLAYER = "KEY_SELECTED_PLAYER";
    private static final String TAG = "PlayersFragment";

    private ArrayList<ListItem> mListItems;
    private ArrayList<ListItem> mListItemsShown;
    private ArrayList<Player> mPlayers;
    private boolean mPulled;
    private Filter mFilter;
    private Listeners mListeners;
    private MenuItem mGo;
    private MenuItem mSearch;
    private MenuItem mSkip;
    private Player mSelectedPlayer;




    public static PlayersFragment create() {
        return new PlayersFragment();
    }


    public void clearSelectedPlayer() {
        if (mPlayers != null && !mPlayers.isEmpty() && mSelectedPlayer != null) {
            notifyDataSetChanged();
            mSelectedPlayer = null;

            if (!isMenuNull()) {
                mGo.setEnabled(false);
            }
        }
    }


    @SuppressWarnings("unchecked")
    private void createListItems() {
        mListItems = new ArrayList<>(mPlayers.size());

        for (final Player player : mPlayers) {
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
        mListItemsShown = mListItems;
    }


    private void fetchPlayers() {
        setLoading(true);

        final ResponseOnUi<ArrayList<Player>> response = new ResponseOnUi<ArrayList<Player>>(TAG, this) {
            @Override
            public void errorOnUi(final Exception e) {
                mPulled = false;
                Console.e(TAG, "Exception when retrieving players!", e);
                showError();
            }


            @Override
            public void successOnUi(final ArrayList<Player> list) {
                mPulled = false;
                mPlayers = list;
                prepareList();
            }
        };

        Players.get(response, mPulled);
    }


    private void findToolbarItems() {
        if (isMenuNull()) {
            final Toolbar toolbar = getToolbar();
            final Menu menu = toolbar.getMenu();
            mGo = menu.findItem(R.id.fragment_players_menu_go);
            mSearch = menu.findItem(R.id.fragment_players_menu_search);
            mSkip = menu.findItem(R.id.fragment_players_menu_skip);

            MenuItemCompat.setOnActionExpandListener(mSearch, this);
            final SearchView searchView = (SearchView) mSearch.getActionView();
            searchView.setOnQueryTextListener(this);
            searchView.setQueryHint(getString(R.string.search_players));
        }
    }


    @Override
    protected int getContentView() {
        return R.layout.fragment_base_list_toolbar_and_statusbar;
    }


    @Override
    protected String getErrorText() {
        return getString(R.string.error_fetching_players);
    }


    @Override
    public String getFragmentName() {
        return TAG;
    }


    @Override
    protected int getOptionsMenu() {
        return R.menu.fragment_players;
    }


    public Player getSelectedPlayer() {
        return mSelectedPlayer;
    }


    private boolean isMenuNull() {
        return mGo == null || mSearch == null || mSkip == null;
    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareViews();

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mPlayers = savedInstanceState.getParcelableArrayList(KEY_PLAYERS);

            if (mPlayers != null && !mPlayers.isEmpty()) {
                mSelectedPlayer = savedInstanceState.getParcelable(KEY_SELECTED_PLAYER);
                prepareList();

                if (mSelectedPlayer != null) {
                    findToolbarItems();
                    mGo.setEnabled(true);
                }
            }
        }
    }


    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mListeners = (Listeners) context;
    }


    public boolean onBackPressed() {
        boolean actionConsumed = false;

        if (!isMenuNull() && MenuItemCompat.isActionViewExpanded(mSearch)) {
            MenuItemCompat.collapseActionView(mSearch);
            actionConsumed = true;
        }

        return actionConsumed;
    }


    @Override
    public void onClick(final CheckableItemView v) {
        mSelectedPlayer = (Player) v.getTag();
        notifyDataSetChanged();

        findToolbarItems();
        mGo.setEnabled(true);
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
        findToolbarItems();

        switch (item.getItemId()) {
            case R.id.fragment_players_menu_go:
                mListeners.onGoClick();
                break;

            case R.id.fragment_players_menu_search:
                MenuItemCompat.expandActionView(mSearch);
                break;

            case R.id.fragment_players_menu_skip:
                mListeners.onSkipClick();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
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
            if (!isMenuNull()) {
                MenuItemCompat.collapseActionView(mSearch);
            }

            mPulled = true;
            refresh();
        }
    }


    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mPlayers != null && !mPlayers.isEmpty()) {
            outState.putParcelableArrayList(KEY_PLAYERS, mPlayers);

            if (mSelectedPlayer != null) {
                outState.putParcelable(KEY_SELECTED_PLAYER, mSelectedPlayer);
            }
        }
    }


    private void prepareList() {
        Collections.sort(mPlayers, Player.ALPHABETICAL_ORDER);
        createListItems();
        setAdapter(new PlayersAdapter());
    }


    @Override
    protected void prepareViews() {
        super.prepareViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final View view = getView();
            final View statusBarBuffer = view.findViewById(R.id.status_bar_buffer);
            Utils.applyStatusBarHeight(statusBarBuffer);
        }

        final Toolbar toolbar = getToolbar();
        toolbar.setTitle(R.string.select_your_tag);
    }


    public void refresh() {
        clearSelectedPlayer();
        fetchPlayers();
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

        findToolbarItems();
        mGo.setVisible(true);
        mSearch.setVisible(true);
        mSkip.setVisible(true);
    }




    private static final class ListItem implements AlphabeticallyComparable, SpecialFilterable {


        private static long sId;

        private long mId;
        private Player mPlayer;
        private String mTitle;
        private Type mType;


        private static ListItem createPlayer(final Player player) {
            final ListItem item = new ListItem();
            item.mPlayer = player;
            item.mType = Type.PLAYER;
            item.mId = sId++;

            return item;
        }


        private static ListItem createTitle(final String title) {
            final ListItem item = new ListItem();
            item.mTitle = title;
            item.mType = Type.TITLE;
            item.mId = sId++;

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
            PLAYER, TITLE;


            @Override
            public String toString() {
                final int resId;

                switch (this) {
                    case PLAYER:
                        resId = R.string.player;
                        break;

                    case TITLE:
                        resId = R.string.title;
                        break;

                    default:
                        throw new IllegalStateException("Type is invalid");
                }

                final Context context = App.getContext();
                return context.getString(resId);
            }
        }


    }


    private final class PlayersAdapter extends RecyclerAdapter {


        private static final String TAG = "PlayersAdapter";


        private PlayersAdapter() {
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
                case PLAYER: {
                    final CheckableItemView civ = ((CheckableItemView.ViewHolder) holder).getView();
                    civ.setText(listItem.mPlayer.getName());
                    civ.setChecked(listItem.mPlayer.equals(mSelectedPlayer));
                    civ.setTag(listItem.mPlayer);
                    break;
                }

                case TITLE: {
                    ((SimpleSeparatorView.ViewHolder) holder).getView().setText(listItem.mTitle);
                    break;
                }

                default:
                    throw new IllegalStateException("Unknown ListItem Type: " + listItem.mType);
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                final int viewType) {
            final ListItem.Type listItemType = ListItem.Type.values()[viewType];
            final RecyclerView.ViewHolder holder;

            switch (listItemType) {
                case PLAYER: {
                    final CheckableItemView civ = CheckableItemView.inflate(parent);
                    civ.setOnClickListener(PlayersFragment.this);
                    holder = civ.getViewHolder();
                    break;
                }

                case TITLE: {
                    holder = SimpleSeparatorView.inflate(parent).getViewHolder();
                    break;
                }

                default:
                    throw new RuntimeException("Unknown ListItem Type: " + listItemType);
            }

            return holder;
        }


    }


    public interface Listeners {


        void onGoClick();


        void onSkipClick();


    }


}

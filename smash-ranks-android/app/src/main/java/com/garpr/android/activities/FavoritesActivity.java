package com.garpr.android.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.garpr.android.R;
import com.garpr.android.misc.FavoritesStore;
import com.garpr.android.misc.ResponseOnUi;
import com.garpr.android.models.Favorites;
import com.garpr.android.models.Favorites.ListItem;
import com.garpr.android.models.Player;
import com.garpr.android.models.Region;
import com.garpr.android.settings.Settings;
import com.garpr.android.views.PlayerItemView;
import com.garpr.android.views.SimpleSeparatorView;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;


public class FavoritesActivity extends BaseToolbarListActivity implements
        PlayerItemView.OnClickListener, PlayerItemView.OnLongClickListener {


    private static final String TAG = "FavoritesActivity";

    private ArrayList<ListItem> mListItems;
    private Favorites mFavorites;




    private void fetchFavorites() {
        setLoading(true);

        FavoritesStore.read(new ResponseOnUi<Favorites>(TAG, this) {
            @Override
            public void errorOnUi(final Exception e) {}


            @Override
            public void successOnUi(final Favorites favorites) {
                mFavorites = favorites;

                if (mFavorites == null || mFavorites.isEmpty()) {
                    showError();
                } else {
                    mListItems = mFavorites.flatten();
                    setAdapter(new FavoritesAdapter());
                }
            }
        });
    }


    @Override
    public String getActivityName() {
        return TAG;
    }


    @Override
    protected String getErrorLine1() {
        return getString(R.string.no_favorites_);
    }


    @Override
    protected String getErrorLine2() {
        return getString(R.string.come_back_here_once_youve_added_some);
    }


    private Region getRegionForPlayer(final Player player) {
        final Set<Entry<Region, ArrayList<Player>>> entrySet = mFavorites.get().entrySet();

        for (final Entry<Region, ArrayList<Player>> entry : entrySet) {
            final ArrayList<Player> players = entry.getValue();

            if (players.contains(player)) {
                return entry.getKey();
            }
        }

        // this should never happen
        throw new RuntimeException("Unable to find region for player " + player.getName());
    }


    @Override
    protected int getSelectedNavigationItemId() {
        return R.id.navigation_view_menu_favorites;
    }


    @Override
    public void onClick(final PlayerItemView v) {
        final Player player = v.getPlayer();
        final Region region = getRegionForPlayer(player);
        Settings.Region.set(region);
        PlayerActivity.IntentBuilder.create(this, player).start(this);
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchFavorites();
    }


    @Override
    public boolean onLongClick(final PlayerItemView v) {
        final Player player = v.getPlayer();

        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.delete_x_from_your_favorites_, player.getName()))
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                        // TODO delete this player
                    }
                })
                .show();

        return true;
    }


    @Override
    public void onRefresh() {
        super.onRefresh();

        if (!isLoading()) {
            fetchFavorites();
        }
    }




    public static class IntentBuilder extends BaseActivity.IntentBuilder {


        public IntentBuilder(final Context context) {
            super(context, FavoritesActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }


    }


    private final class FavoritesAdapter extends RecyclerView.Adapter {


        @Override
        public int getItemCount() {
            return mListItems.size();
        }


        @Override
        public int getItemViewType(final int position) {
            return mListItems.get(position).getType().ordinal();
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final ListItem listItem = mListItems.get(position);

            switch (listItem.getType()) {
                case PLAYER:
                    ((PlayerItemView.ViewHolder) holder).getView()
                            .setPlayer(listItem.getPlayer());
                    break;

                case REGION:
                    ((SimpleSeparatorView.ViewHolder) holder).getView()
                            .setText(listItem.getRegion().getName());
                    break;

                default:
                    throw new RuntimeException("Unknown ListItem Type: " + listItem.getType());
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                final int viewType) {
            final ListItem.Type listItemType = ListItem.Type.values()[viewType];
            final RecyclerView.ViewHolder holder;

            switch (listItemType) {
                case PLAYER:
                    final PlayerItemView piv = PlayerItemView.inflate(parent);
                    piv.setOnClickListener(FavoritesActivity.this);
                    piv.setOnLongClickListener(FavoritesActivity.this);
                    holder = piv.getViewHolder();
                    break;

                case REGION:
                    holder = SimpleSeparatorView.inflate(parent).getViewHolder();
                    break;

                default:
                    throw new RuntimeException("Unknown ListItem Type: " + listItemType);
            }

            return holder;
        }


    }


}

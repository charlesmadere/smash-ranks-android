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


public class FavoritesActivity extends BaseToolbarListActivity implements
        PlayerItemView.OnClickListener, PlayerItemView.OnLongClickListener {


    private static final String TAG = "FavoritesActivity";

    private ArrayList<ListItem> mListItems;
    private Favorites mFavorites;




    private void fetchFavorites() {
        setLoading(true);

        FavoritesStore.read(new ResponseOnUi<Favorites>(TAG, this) {
            @Override
            public void errorOnUi(final Exception e) {
                throw new UnsupportedOperationException("this should never happen", e);
            }


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


    @Override
    protected int getSelectedNavigationItemId() {
        return R.id.navigation_view_menu_favorites;
    }


    @Override
    public void onClick(final PlayerItemView v) {
        final Player player = v.getPlayer();
        Settings.Region.set(mFavorites.findRegionForPlayer(player));
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

                        final Region region = mFavorites.findRegionForPlayer(player);
                        mFavorites.remove(player);

                        for (int i = 0; i < mListItems.size(); ++i) {
                            final ListItem li = mListItems.get(i);

                            if (li.isPlayer() && li.getPlayer().equals(player)) {
                                mListItems.remove(i);
                                break;
                            }
                        }

                        if (!mFavorites.hasPlayers(region)) {
                            mFavorites.remove(region);

                            for (int i = 0; i < mListItems.size(); ++i) {
                                final ListItem li = mListItems.get(i);

                                if (li.isRegion() && li.getRegion().equals(region)) {
                                    mListItems.remove(i);
                                    break;
                                }
                            }
                        }

                        notifyDataSetChanged();
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


    @Override
    protected void onStop() {
        if (mFavorites != null) {
            FavoritesStore.write(mFavorites);
        }

        super.onStop();
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
        public long getItemId(final int position) {
            return mListItems.get(position).getId();
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

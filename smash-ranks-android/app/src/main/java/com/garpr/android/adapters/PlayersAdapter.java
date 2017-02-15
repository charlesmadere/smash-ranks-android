package com.garpr.android.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.garpr.android.R;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.PlayersBundle;

public class PlayersAdapter extends BaseAdapter<AbsPlayer> {

    public PlayersAdapter(@NonNull final Context context) {
        super(context);
        setHasStableIds(true);
    }

    public PlayersAdapter(@NonNull final LayoutInflater layoutInflater) {
        super(layoutInflater);
        setHasStableIds(true);
    }

    @Override
    public long getItemId(final int position) {
        return getItem(position).hashCode();
    }

    @LayoutRes
    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_player;
    }

    public void set(@Nullable final PlayersBundle bundle) {
        if (bundle != null && bundle.hasPlayers()) {
            set(bundle.getPlayers());
        } else {
            clear();
        }
    }

}

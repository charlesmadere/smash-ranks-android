package com.garpr.android.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.garpr.android.R;
import com.garpr.android.models.AbsPlayer;

public class PlayersSelectionAdapter extends BaseAdapter<AbsPlayer> {

    public PlayersSelectionAdapter(@NonNull final Context context) {
        super(context);
        setHasStableIds(true);
    }

    public PlayersSelectionAdapter(@NonNull final LayoutInflater layoutInflater) {
        super(layoutInflater);
        setHasStableIds(true);
    }

    @LayoutRes
    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_player_selection;
    }

}

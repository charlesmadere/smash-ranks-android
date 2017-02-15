package com.garpr.android.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.garpr.android.R;
import com.garpr.android.models.AbsTournament;

public class TournamentsAdapter extends BaseAdapter<AbsTournament> {

    public TournamentsAdapter(@NonNull final Context context) {
        super(context);
        setHasStableIds(true);
    }

    public TournamentsAdapter(@NonNull final LayoutInflater layoutInflater) {
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
        return R.layout.item_tournament;
    }

}

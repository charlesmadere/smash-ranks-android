package com.garpr.android.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.garpr.android.R;
import com.garpr.android.models.Tournament;

public class TournamentsAdapter extends BaseAdapter<Tournament> {

    public TournamentsAdapter(@NonNull final Context context) {
        super(context);
    }

    public TournamentsAdapter(@NonNull final LayoutInflater layoutInflater) {
        super(layoutInflater);
    }

    @LayoutRes
    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_tournament;
    }

}

package com.garpr.android.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.garpr.android.R;
import com.garpr.android.models.AbsTournament;
import com.garpr.android.models.TournamentsBundle;

public class TournamentsAdapter extends BaseAdapter<AbsTournament> {

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

    public void set(@Nullable final TournamentsBundle bundle) {
        if (bundle != null && bundle.hasTournaments()) {
            set(bundle.getTournaments());
        } else {
            clear();
        }
    }

}

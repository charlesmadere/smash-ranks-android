package com.garpr.android.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.garpr.android.R;
import com.garpr.android.models.Ranking;
import com.garpr.android.models.RankingsBundle;

public class RankingsAdapter extends BaseAdapter<Ranking> {

    public RankingsAdapter(@NonNull final Context context) {
        super(context);
    }

    public RankingsAdapter(@NonNull final LayoutInflater layoutInflater) {
        super(layoutInflater);
    }

    @LayoutRes
    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_ranking;
    }

    public void set(@Nullable final RankingsBundle bundle) {
        if (bundle != null && bundle.hasRankings()) {
            set(bundle.getRankings());
        } else {
            clear();
        }
    }

}
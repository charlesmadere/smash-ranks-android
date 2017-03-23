package com.garpr.android.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.garpr.android.R;
import com.garpr.android.models.Region;
import com.garpr.android.models.RegionsBundle;

public class RegionsSelectionAdapter extends BaseAdapter<Region> {

    public RegionsSelectionAdapter(@NonNull final Context context) {
        super(context);
        setHasStableIds(true);
    }

    public RegionsSelectionAdapter(@NonNull final LayoutInflater layoutInflater) {
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
        return R.layout.item_region_selection;
    }

    public void set(@Nullable final RegionsBundle bundle) {
        if (bundle != null && bundle.hasRegions()) {
            set(bundle.getRegions());
        } else {
            clear();
        }
    }

}

package com.garpr.android.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.garpr.android.R;
import com.garpr.android.misc.Timber;

public class TimberEntriesAdapter extends BaseAdapter<Timber.Entry> {

    public TimberEntriesAdapter(@NonNull final Context context) {
        super(context);
    }

    public TimberEntriesAdapter(@NonNull final LayoutInflater layoutInflater) {
        super(layoutInflater);
    }

    @LayoutRes
    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_timber_entry;
    }

}

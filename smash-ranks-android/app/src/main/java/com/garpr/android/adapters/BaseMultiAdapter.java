package com.garpr.android.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import java.util.Map;

public abstract class BaseMultiAdapter extends BaseAdapter<Object> {

    private final Map<Class, Integer> mLayoutKeyMap;


    public BaseMultiAdapter(@NonNull final Context context,
            @NonNull final Map<Class, Integer> layoutKeyMap) {
        super(context);
        mLayoutKeyMap = layoutKeyMap;
    }

    public BaseMultiAdapter(@NonNull final LayoutInflater layoutInflater,
            @NonNull final Map<Class, Integer> layoutKeyMap) {
        super(layoutInflater);
        mLayoutKeyMap = layoutKeyMap;
    }

    @Override
    public int getItemViewType(final int position) {
        final Object item = getItem(position);
        return mLayoutKeyMap.get(item.getClass());
    }

}

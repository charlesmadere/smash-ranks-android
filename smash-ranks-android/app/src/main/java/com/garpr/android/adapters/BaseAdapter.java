package com.garpr.android.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapterViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<T> mItems;


    public BaseAdapter(@NonNull final Context context) {
        this(LayoutInflater.from(context));
    }

    public BaseAdapter(@NonNull final LayoutInflater layoutInflater) {
        mLayoutInflater = layoutInflater;
        mItems = new ArrayList<>();
    }

    public void add(@Nullable final T item) {
        if (item != null) {
            mItems.add(item);
            notifyDataSetChanged();
        }
    }

    public void add(@Nullable final List<T> items) {
        if (items != null && !items.isEmpty()) {
            for (final T item : items) {
                if (item != null) {
                    mItems.add(item);
                }
            }

            notifyDataSetChanged();
        }
    }

    public void clear() {
        if (!mItems.isEmpty()) {
            mItems.clear();
            notifyDataSetChanged();
        }
    }

    public Context getContext() {
        return mLayoutInflater.getContext();
    }

    public T getItem(final int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public List<T> getItems() {
        if (isEmpty()) {
            return Collections.emptyList();
        } else {
            return new ArrayList<>(mItems);
        }
    }

    @LayoutRes
    @Override
    public abstract int getItemViewType(final int position);

    public boolean isEmpty() {
        return mItems.isEmpty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(final BaseAdapterViewHolder holder, final int position) {
        final T item = mItems.get(position);
        ((BaseAdapterView) holder.itemView).setContent(item);
    }

    @Override
    public BaseAdapterViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = mLayoutInflater.inflate(viewType, parent, false);
        return new BaseAdapterViewHolder(view);
    }

    public void set(@Nullable final List<T> items) {
        mItems.clear();

        if (items != null && !items.isEmpty()) {
            mItems.addAll(items);
        }

        notifyDataSetChanged();
    }

}

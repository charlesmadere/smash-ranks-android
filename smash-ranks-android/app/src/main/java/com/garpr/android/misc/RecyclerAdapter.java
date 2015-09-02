package com.garpr.android.misc;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


public abstract class RecyclerAdapter<T extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<T> {


    private boolean mNotifyDataSetChanged;




    protected RecyclerAdapter(final RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        setHasStableIds(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private void notifyDataSetChanged() {
                mNotifyDataSetChanged = false;
                RecyclerAdapter.this.notifyDataSetChanged();
            }


            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE && mNotifyDataSetChanged) {
                    notifyDataSetChanged();
                }
            }
        });
    }


    public void dataSetChanged() {
        try {
            notifyDataSetChanged();
        } catch (final IllegalStateException e) {
            Console.e(getAdapterName(), "Exception when using notifyDataSetChanged()", e);
            mNotifyDataSetChanged = true;
        }
    }


    public abstract String getAdapterName();


    @Override
    public long getItemId(final int position) {
        return (long) position;
    }


    @Override
    public abstract void onBindViewHolder(final T holder, final int position);


    @Override
    public abstract T onCreateViewHolder(final ViewGroup parent, final int viewType);


    @Override
    public final String toString() {
        return getAdapterName();
    }


}

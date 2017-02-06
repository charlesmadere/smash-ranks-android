package com.garpr.android.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.garpr.android.R;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.adapters.StringsAdapter;

import java.util.List;

public class AliasesListView extends RecyclerView implements BaseAdapterView<List<String>> {

    public static AliasesListView inflate(final Context context) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        return (AliasesListView) inflater.inflate(R.layout.list_aliases, null);
    }

    public AliasesListView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public AliasesListView(final Context context, @Nullable final AttributeSet attrs,
            final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setContent(final List<String> content) {
        final StringsAdapter adapter = new StringsAdapter(getContext());
        adapter.set(content);
        setAdapter(adapter);
    }

}

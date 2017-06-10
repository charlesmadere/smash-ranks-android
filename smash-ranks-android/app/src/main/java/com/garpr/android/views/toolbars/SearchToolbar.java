package com.garpr.android.views.toolbars;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.garpr.android.R;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.SearchQueryHandle;
import com.garpr.android.misc.Searchable;

public abstract class SearchToolbar extends MenuToolbar implements
        MenuItemCompat.OnActionExpandListener, SearchQueryHandle, SearchView.OnQueryTextListener {

    protected MenuItem mSearchMenuItem;
    protected SearchView mSearchView;


    public SearchToolbar(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchToolbar(final Context context, @Nullable final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void closeSearchLayout() {
        if (isSearchLayoutExpanded()) {
            MenuItemCompat.collapseActionView(mSearchMenuItem);
        }
    }

    @Nullable
    @Override
    public CharSequence getSearchQuery() {
        return mSearchView == null ? null : mSearchView.getQuery();
    }

    public boolean isSearchLayoutExpanded() {
        return mSearchMenuItem != null && MenuItemCompat.isActionViewExpanded(mSearchMenuItem);
    }

    @Override
    public void onCreateOptionsMenu(final MenuInflater inflater, final Menu menu) {
        mSearchMenuItem = menu.findItem(R.id.miSearch);
        MenuItemCompat.setOnActionExpandListener(mSearchMenuItem, this);

        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
        mSearchView.setQueryHint(getResources().getText(R.string.search_));
        mSearchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(inflater, menu);
    }

    @Override
    public boolean onMenuItemActionCollapse(final MenuItem item) {
        postRefreshMenu();
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(final MenuItem item) {
        postRefreshMenu();
        return true;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        final Activity activity = MiscUtils.optActivity(getContext());

        if (activity instanceof Searchable) {
            ((Searchable) activity).search(newText);
        }

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        onQueryTextChange(query);
        return false;
    }

    @Override
    public void onRefreshMenu() {
        final Activity activity = MiscUtils.optActivity(getContext());
        mSearchMenuItem.setVisible(activity instanceof Listener &&
                ((Listener) activity).showSearchButton());
    }


    public interface Listener {
        boolean showSearchButton();
    }

}

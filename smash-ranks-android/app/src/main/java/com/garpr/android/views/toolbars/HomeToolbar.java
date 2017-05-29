package com.garpr.android.views.toolbars;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.Heartbeat;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.SearchQueryHandle;
import com.garpr.android.misc.Searchable;
import com.garpr.android.models.Region;

import javax.inject.Inject;

public class HomeToolbar extends MenuToolbar implements Heartbeat,
        IdentityManager.OnIdentityChangeListener, MenuItemCompat.OnActionExpandListener,
        RegionManager.OnRegionChangeListener, SearchQueryHandle, SearchView.OnQueryTextListener {

    private MenuItem mActivityRequirementsMenuItem;
    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;

    @Inject
    IdentityManager mIdentityManager;

    @Inject
    RegionManager mRegionManager;


    public HomeToolbar(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeToolbar(final Context context, @Nullable final AttributeSet attrs,
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

    @Override
    public boolean isAlive() {
        return ViewCompat.isAttachedToWindow(this);
    }

    public boolean isSearchLayoutExpanded() {
        return mSearchMenuItem != null && MenuItemCompat.isActionViewExpanded(mSearchMenuItem);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }

        refreshMenu();
        mIdentityManager.addListener(this);
        mRegionManager.addListener(this);
    }

    @Override
    public void onCreateOptionsMenu(final MenuInflater inflater, final Menu menu) {
        super.onCreateOptionsMenu(inflater, menu);
        inflater.inflate(R.menu.toolbar_home, menu);

        mSearchMenuItem = menu.findItem(R.id.miSearch);
        MenuItemCompat.setOnActionExpandListener(mSearchMenuItem, this);

        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
        mSearchView.setQueryHint(getResources().getText(R.string.search_));
        mSearchView.setOnQueryTextListener(this);

        mActivityRequirementsMenuItem = menu.findItem(R.id.miActivityRequirements);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIdentityManager.removeListener(this);
        mRegionManager.removeListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (!isInEditMode()) {
            App.get().getAppComponent().inject(this);
            mIdentityManager.addListener(this);
            mRegionManager.addListener(this);
        }
    }

    @Override
    public void onIdentityChange(final IdentityManager identityManager) {
        if (isAlive()) {
            refreshMenu();
        }
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
        return onQueryTextChange(query);
    }

    @Override
    public void onRegionChange(final RegionManager regionManager) {
        if (isAlive()) {
            closeSearchLayout();
            refreshMenu();
        }
    }

    private void postRefreshMenu() {
        post(new Runnable() {
            @Override
            public void run() {
                if (isAlive()) {
                    refreshMenu();
                }
            }
        });
    }

    @Override
    public void refreshMenu() {
        if (!isMenuCreated()) {
            return;
        }

        if (isSearchLayoutExpanded()) {
            mSearchMenuItem.setVisible(false);
            mActivityRequirementsMenuItem.setVisible(false);
        } else {
            mSearchMenuItem.setVisible(true);

            final Region region = mRegionManager.getRegion(getContext());
            mActivityRequirementsMenuItem.setVisible(region.hasActivityRequirements());
        }

        final Menu menu = getMenu();
        menu.findItem(R.id.miViewYourself).setVisible(mIdentityManager.hasIdentity());
    }

}

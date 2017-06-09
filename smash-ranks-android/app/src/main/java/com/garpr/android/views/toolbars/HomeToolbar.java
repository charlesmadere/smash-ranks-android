package com.garpr.android.views.toolbars;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.models.Region;

import javax.inject.Inject;

public class HomeToolbar extends SearchToolbar implements IdentityManager.OnIdentityChangeListener,
        RegionManager.OnRegionChangeListener {

    private MenuItem mActivityRequirementsMenuItem;

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
        inflater.inflate(R.menu.toolbar_home, menu);
        super.onCreateOptionsMenu(inflater, menu);
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
    public void onRefreshMenu() {
        super.onRefreshMenu();

        if (isSearchLayoutExpanded()) {
            mActivityRequirementsMenuItem.setVisible(false);
        } else {
            final Region region = mRegionManager.getRegion(getContext());
            mActivityRequirementsMenuItem.setVisible(region.hasActivityRequirements());
        }

        final Menu menu = getMenu();
        menu.findItem(R.id.miViewYourself).setVisible(mIdentityManager.hasIdentity());
    }

    @Override
    public void onRegionChange(final RegionManager regionManager) {
        if (isAlive()) {
            closeSearchLayout();
            refreshMenu();
        }
    }

}

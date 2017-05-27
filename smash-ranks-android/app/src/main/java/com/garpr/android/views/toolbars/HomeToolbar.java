package com.garpr.android.views.toolbars;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.Heartbeat;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.SearchQueryHandle;
import com.garpr.android.models.Region;
import com.garpr.android.views.SearchLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeToolbar extends Toolbar implements Heartbeat,
        IdentityManager.OnIdentityChangeListener, Toolbar.OnMenuItemClickListener,
        RegionManager.OnRegionChangeListener, SearchLayout.Listeners, SearchQueryHandle {

    @Inject
    IdentityManager mIdentityManager;

    @Inject
    RegionManager mRegionManager;

    @BindView(R.id.ibActivityRequirements)
    ImageButton mActivityRequirementsButton;

    @BindView(R.id.ibSearch)
    ImageButton mSearchButton;

    @BindView(R.id.searchLayout)
    SearchLayout mSearchLayout;


    public HomeToolbar(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeToolbar(final Context context, @Nullable final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void closeSearchLayout() {
        if (isSearchLayoutExpanded()) {
            mSearchLayout.close();
        }
    }

    @Nullable
    @Override
    public CharSequence getSearchQuery() {
        return mSearchLayout.getSearchQuery();
    }

    @Override
    public boolean isAlive() {
        return ViewCompat.isAttachedToWindow(this);
    }

    public boolean isSearchLayoutExpanded() {
        return mSearchLayout.isExpanded();
    }

    @OnClick(R.id.ibActivityRequirements)
    void onActivityRequirementsButtonClick() {
        final Activity activity = MiscUtils.optActivity(getContext());

        if (activity instanceof Listeners) {
            ((Listeners) activity).onActivityRequirementsButtonClick();
        }
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
        }

        ButterKnife.bind(this);
        inflateMenu(R.menu.toolbar_home);
        setOnMenuItemClickListener(this);
        mSearchLayout.setListeners(this);
        refreshMenu();

        if (isInEditMode()) {
            return;
        }

        mIdentityManager.addListener(this);
        mRegionManager.addListener(this);
    }

    @Override
    public void onIdentityChange(final IdentityManager identityManager) {
        if (isAlive()) {
            refreshMenu();
        }
    }

    @Override
    public boolean onMenuItemClick(final MenuItem item) {
        final Activity activity = MiscUtils.optActivity(getContext());

        if (activity instanceof Listeners) {
            return ((Listeners) activity).onMenuItemClick(item);
        } else {
            return false;
        }
    }

    @Override
    public void onRegionChange(final RegionManager regionManager) {
        if (isAlive()) {
            mSearchLayout.close();
            refreshMenu();
        }
    }

    @OnClick(R.id.ibSearch)
    void onSearchButtonClick() {
        mSearchLayout.expand();
        refreshMenu();
    }

    @Override
    public void onSearchFieldClosed(final SearchLayout searchLayout) {
        refreshMenu();
    }

    @Override
    public void onSearchFieldExpanded(final SearchLayout searchLayout) {
        refreshMenu();
    }

    @Override
    public void onSearchFieldTextChanged(final SearchLayout searchLayout) {
        final Activity activity = MiscUtils.optActivity(getContext());

        if (activity instanceof Listeners) {
            ((Listeners) activity).onSearchFieldTextChanged(mSearchLayout);
        }
    }

    private void refreshMenu() {
        if (mSearchLayout.isExpanded()) {
            mSearchButton.setVisibility(GONE);
            mActivityRequirementsButton.setVisibility(GONE);
        } else {
            mSearchButton.setVisibility(VISIBLE);

            final Region region = mRegionManager.getRegion(getContext());
            mActivityRequirementsButton.setVisibility(region.hasActivityRequirements() ? VISIBLE : GONE);
        }

        final Menu menu = getMenu();
        menu.findItem(R.id.miViewYourself).setVisible(mIdentityManager.hasIdentity());
    }


    public interface Listeners {
        void onActivityRequirementsButtonClick();
        boolean onMenuItemClick(final MenuItem item);
        void onSearchFieldTextChanged(final SearchLayout searchLayout);
    }

}

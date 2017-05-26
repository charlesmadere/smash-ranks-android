package com.garpr.android.views.toolbars;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageButton;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.misc.Heartbeat;
import com.garpr.android.misc.IdentityManager;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.SearchQueryHandle;
import com.garpr.android.views.SearchLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeToolbar extends Toolbar implements Heartbeat,
        IdentityManager.OnIdentityChangeListener, RegionManager.OnRegionChangeListener,
        SearchLayout.Listeners, SearchQueryHandle {

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

    public void createOptionsMenu(final MenuInflater inflater, final Menu menu) {
        inflater.inflate(R.menu.toolbar_home, menu);
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
        mSearchLayout.setListeners(this);

        if (!isInEditMode()) {
            mIdentityManager.addListener(this);
            mRegionManager.addListener(this);
        }
    }

    @Override
    public void onIdentityChange(final IdentityManager identityManager) {

    }

    @Override
    public void onRegionChange(final RegionManager regionManager) {

    }

    @OnClick(R.id.ibSearch)
    void onSearchButtonClick() {
        mSearchLayout.expand();
        refreshButtonVisibility();
    }

    @Override
    public void onSearchFieldClosed(final SearchLayout searchLayout) {
        refreshButtonVisibility();
    }

    @Override
    public void onSearchFieldExpanded(final SearchLayout searchLayout) {
        refreshButtonVisibility();
    }

    @Override
    public void onSearchFieldTextChanged(final SearchLayout searchLayout) {
        final Activity activity = MiscUtils.optActivity(getContext());

        if (activity instanceof Listeners) {
            ((Listeners) activity).onSearchFieldTextChanged(mSearchLayout);
        }
    }

    private void refreshButtonVisibility() {
        if (mSearchLayout.isExpanded()) {
            mSearchButton.setVisibility(GONE);
            mActivityRequirementsButton.setVisibility(GONE);
        } else {
            mSearchButton.setVisibility(VISIBLE);
            mActivityRequirementsButton.setVisibility(VISIBLE);
        }
    }


    public interface Listeners {
        void onActivityRequirementsButtonClick();
        void onSearchFieldTextChanged(@Nullable final SearchLayout searchLayout);
    }

}

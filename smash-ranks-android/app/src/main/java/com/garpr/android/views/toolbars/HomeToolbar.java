package com.garpr.android.views.toolbars;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;

import com.garpr.android.R;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.views.SearchLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeToolbar extends Toolbar implements SearchLayout.Listeners {

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
        if (mSearchLayout.isExpanded()) {
            mSearchLayout.close();
        }
    }

    @OnClick(R.id.ibActivityRequirements)
    void onActivityRequirementsButtonClick() {
        final Activity activity = MiscUtils.optActivity(getContext());

        if (activity instanceof Listeners) {
            ((Listeners) activity).onActivityRequirementsButtonClick();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.toolbar_home, this);
        ButterKnife.bind(this);
        mSearchLayout.setListeners(this);
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

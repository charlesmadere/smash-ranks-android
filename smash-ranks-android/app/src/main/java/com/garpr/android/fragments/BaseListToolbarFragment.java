package com.garpr.android.fragments;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.garpr.android.R;


public abstract class BaseListToolbarFragment extends BaseListFragment implements
        Toolbar.OnMenuItemClickListener {


    private Toolbar mToolbar;




    @Override
    protected void findViews() {
        super.findViews();
        final View view = getView();
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
    }


    @Override
    protected int getContentView() {
        return R.layout.fragment_base_list_toolbar;
    }


    protected int getOptionsMenu() {
        return 0;
    }


    protected Toolbar getToolbar() {
        return mToolbar;
    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final int optionsMenu = getOptionsMenu();

        if (optionsMenu != 0) {
            mToolbar.inflateMenu(optionsMenu);
            mToolbar.setOnMenuItemClickListener(this);
        }
    }


    @Override
    public final boolean onMenuItemClick(final MenuItem item) {
        return onOptionsItemSelected(item);
    }


}

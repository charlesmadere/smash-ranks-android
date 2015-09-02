package com.garpr.android.fragments;


import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.garpr.android.R;
import com.garpr.android.misc.RecyclerAdapter;
import com.garpr.android.views.CheckableItemView;


public class ToolbarRegionsFragment extends RegionsFragment implements
        Toolbar.OnMenuItemClickListener {


    private static final String TAG = "ToolbarRegionsFragment";

    private MenuItem mNext;
    private NextListener mListener;
    private Toolbar mToolbar;




    public static ToolbarRegionsFragment create() {
        return new ToolbarRegionsFragment();
    }


    private void findToolbarItems() {
        if (mNext == null) {
            final Menu menu = mToolbar.getMenu();
            mNext = menu.findItem(R.id.fragment_toolbar_regions_menu_next);
        }
    }


    @Override
    protected void findViews() {
        super.findViews();

        final View view = getView();
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
    }


    @Override
    protected int getContentView() {
        return R.layout.fragment_base_list_toolbar_and_statusbar;
    }


    @Override
    public String getFragmentName() {
        return TAG;
    }


    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mListener = (NextListener) context;
    }


    @Override
    public void onClick(final CheckableItemView v) {
        super.onClick(v);

        findToolbarItems();
        mNext.setEnabled(true);
    }


    @Override
    public boolean onMenuItemClick(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_toolbar_regions_menu_next:
                mListener.onRegionNextClick();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }


    @Override
    protected void prepareViews() {
        super.prepareViews();
        mToolbar.inflateMenu(R.menu.fragment_toolbar_regions);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setTitle(R.string.select_your_region);
        findToolbarItems();
    }


    @Override
    protected void setAdapter(final RecyclerAdapter adapter) {
        super.setAdapter(adapter);
        findToolbarItems();

        if (mSelectedRegion != null) {
            mNext.setEnabled(true);
        }
    }




    public interface NextListener {


        void onRegionNextClick();


    }


}

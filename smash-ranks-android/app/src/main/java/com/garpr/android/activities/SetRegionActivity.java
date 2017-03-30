package com.garpr.android.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.adapters.RegionsSelectionAdapter;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.misc.ResultCodes;
import com.garpr.android.models.Region;
import com.garpr.android.models.RegionsBundle;
import com.garpr.android.networking.ApiCall;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;
import com.garpr.android.views.RegionSelectionItemView;

import javax.inject.Inject;

import butterknife.BindView;

public class SetRegionActivity extends BaseActivity implements ApiListener<RegionsBundle>,
        RegionSelectionItemView.Listeners, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "SetRegionActivity";

    private MenuItem mSaveMenuItem;
    private Region mSelectedRegion;
    private RegionsBundle mRegionsBundle;
    private RegionsSelectionAdapter mAdapter;

    @Inject
    RegionManager mRegionManager;

    @Inject
    ServerApi mServerApi;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.empty)
    View mEmpty;

    @BindView(R.id.error)
    View mError;


    public static Intent getLaunchIntent(final Context context) {
        return new Intent(context, SetRegionActivity.class);
    }

    @Override
    public void failure() {
        mSelectedRegion = null;
        mRegionsBundle = null;
        showError();
    }

    private void fetchRegionsBundle() {
        mSelectedRegion = null;
        mRegionsBundle = null;
        mRefreshLayout.setRefreshing(true);
        mServerApi.getRegions(new ApiCall<>(this));
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Nullable
    @Override
    public Region getSelectedRegion() {
        if (mSelectedRegion == null) {
            return mRegionManager.getRegion();
        } else {
            return mSelectedRegion;
        }
    }

    @Override
    protected void navigateUp() {
        if (mSelectedRegion == null) {
            super.navigateUp();
            return;
        }

        new AlertDialog.Builder(this)
                .setMessage(R.string.youve_selected_a_region_but_havent_saved)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        SetRegionActivity.super.navigateUp();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        if (mSelectedRegion == null) {
            super.onBackPressed();
            return;
        }

        new AlertDialog.Builder(this)
                .setMessage(R.string.youve_selected_a_region_but_havent_saved)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        SetRegionActivity.super.onBackPressed();
                    }
                })
                .show();
    }

    @Override
    public void onClick(final RegionSelectionItemView v) {
        final Region region = v.getContent();

        if (region.equals(mRegionManager.getRegion())) {
            mSelectedRegion = null;
        } else {
            mSelectedRegion = region;
        }

        refreshMenu();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get().getAppComponent().inject(this);
        setContentView(R.layout.activity_set_region);

        fetchRegionsBundle();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_set_region, menu);
        mSaveMenuItem = menu.findItem(R.id.miSave);
        refreshMenu();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miSave:
                save();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        fetchRegionsBundle();
    }

    @Override
    protected void onViewsBound() {
        super.onViewsBound();

        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RegionsSelectionAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void refreshMenu() {
        if (mSaveMenuItem == null) {
            return;
        }

        if (mRefreshLayout.isRefreshing() || mAdapter.isEmpty() ||
                mEmpty.getVisibility() == View.VISIBLE || mError.getVisibility() == View.VISIBLE) {
            mSaveMenuItem.setEnabled(false);
            mSaveMenuItem.setVisible(false);
        } else {
            mSaveMenuItem.setEnabled(mSelectedRegion != null);
            mSaveMenuItem.setVisible(true);
        }
    }

    private void save() {
        mRegionManager.setRegion(mSelectedRegion);
        Toast.makeText(this, R.string.region_saved_, Toast.LENGTH_LONG).show();
        setResult(ResultCodes.REGION_SELECTED.mValue);
        supportFinishAfterTransition();
    }

    private void showEmpty() {
        mAdapter.clear();
        mRecyclerView.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mEmpty.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
        refreshMenu();
    }

    private void showError() {
        mAdapter.clear();
        mRecyclerView.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
        refreshMenu();
    }

    private void showRegionsBundle() {
        mAdapter.set(mRegionsBundle);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.setEnabled(false);
        refreshMenu();
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

    @Override
    public void success(@Nullable final RegionsBundle regionsBundle) {
        mRegionsBundle = regionsBundle;

        if (mRegionsBundle != null && mRegionsBundle.hasRegions()) {
            showRegionsBundle();
        } else {
            showEmpty();
        }
    }

}

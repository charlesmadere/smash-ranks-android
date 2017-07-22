package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.RegionsSelectionAdapter
import com.garpr.android.extensions.subtitle
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.ResultCodes
import com.garpr.android.models.Region
import com.garpr.android.models.RegionsBundle
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.views.RegionSelectionItemView
import kotterknife.bindView
import javax.inject.Inject

class SetRegionActivity : BaseActivity(), ApiListener<RegionsBundle>,
        RegionSelectionItemView.Listeners, SwipeRefreshLayout.OnRefreshListener {

    private var mSaveMenuItem: MenuItem? = null
    private var mSelectedRegion: Region? = null
    private var mRegionsBundle: RegionsBundle? = null
    lateinit private var mAdapter: RegionsSelectionAdapter

    @Inject
    lateinit protected var mRegionManager: RegionManager

    @Inject
    lateinit protected var mServerApi: ServerApi

    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val mRefreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val mEmpty: View by bindView(R.id.empty)
    private val mError: View by bindView(R.id.error)


    companion object {
        private const val TAG = "SetRegionActivity"

        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, SetRegionActivity::class.java)
        }
    }

    override val activityName = TAG

    override fun failure(errorCode: Int) {
        mSelectedRegion = null
        mRegionsBundle = null
        showError()
    }

    private fun fetchRegionsBundle() {
        mSelectedRegion = null
        mRegionsBundle = null
        mRefreshLayout.isRefreshing = true
        mServerApi.getRegions(ApiCall(this))
    }

    override fun navigateUp() {
        if (mSelectedRegion == null) {
            super.navigateUp()
            return
        }

        AlertDialog.Builder(this)
                .setMessage(R.string.youve_selected_a_region_but_havent_saved)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes) { dialog, which ->
                    super@SetRegionActivity.navigateUp() }
                .show()
    }

    override fun onBackPressed() {
        if (mSelectedRegion == null) {
            super.onBackPressed()
            return
        }

        AlertDialog.Builder(this)
                .setMessage(R.string.youve_selected_a_region_but_havent_saved)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes) { dialog, which ->
                    super@SetRegionActivity.onBackPressed() }
                .show()
    }

    override fun onClick(v: RegionSelectionItemView) {
        val region = v.mContent

        if (region == mRegionManager.region) {
            mSelectedRegion = null
        } else {
            mSelectedRegion = region
        }

        refreshMenu()
        mAdapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_set_region)
        subtitle = mRegionManager.getRegion(this).displayName

        fetchRegionsBundle()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_set_region, menu)
        mSaveMenuItem = menu.findItem(R.id.miSave)
        refreshMenu()

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miSave -> {
                save()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        fetchRegionsBundle()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        mRefreshLayout.setOnRefreshListener(this)
        mRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mRecyclerView.setHasFixedSize(true)
        mAdapter = RegionsSelectionAdapter(this)
        mRecyclerView.adapter = mAdapter
    }

    private fun refreshMenu() {
        val saveMenuItem = mSaveMenuItem ?: return

        if (mRefreshLayout.isRefreshing || mAdapter.isEmpty ||
                mEmpty.visibility == View.VISIBLE || mError.visibility == View.VISIBLE) {
            saveMenuItem.isEnabled = false
            saveMenuItem.isVisible = false
        } else {
            saveMenuItem.isEnabled = mSelectedRegion != null
            saveMenuItem.isVisible = true
        }
    }

    private fun save() {
        mRegionManager.region = mSelectedRegion!!
        Toast.makeText(this, R.string.region_saved_, Toast.LENGTH_LONG).show()
        setResult(ResultCodes.REGION_SELECTED.mValue)
        supportFinishAfterTransition()
    }

    override val selectedRegion: Region?
        get() { return if (mSelectedRegion == null) mRegionManager.region else mSelectedRegion }

    private fun showEmpty() {
        mAdapter.clear()
        mRecyclerView.visibility = View.GONE
        mError.visibility = View.GONE
        mEmpty.visibility = View.VISIBLE
        mRefreshLayout.isRefreshing = false
        refreshMenu()
    }

    private fun showError() {
        mAdapter.clear()
        mRecyclerView.visibility = View.GONE
        mEmpty.visibility = View.GONE
        mError.visibility = View.VISIBLE
        mRefreshLayout.isRefreshing = false
        refreshMenu()
    }

    private fun showRegionsBundle() {
        mAdapter.set(mRegionsBundle)
        mEmpty.visibility = View.GONE
        mError.visibility = View.GONE
        mRecyclerView.visibility = View.VISIBLE
        mRefreshLayout.isRefreshing = false
        mRefreshLayout.isEnabled = false
        refreshMenu()
    }

    override val showUpNavigation = true

    override fun success(regionsBundle: RegionsBundle?) {
        mRegionsBundle = regionsBundle

        if (regionsBundle != null && regionsBundle.hasRegions()) {
            showRegionsBundle()
        } else {
            showEmpty()
        }
    }

}

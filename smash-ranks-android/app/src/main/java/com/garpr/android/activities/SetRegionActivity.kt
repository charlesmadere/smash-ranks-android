package com.garpr.android.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.RegionsSelectionAdapter
import com.garpr.android.extensions.subtitle
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.ListUtils
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

    private var saveMenuItem: MenuItem? = null
    private var _selectedRegion: Region? = null
    private var regionsBundle: RegionsBundle? = null
    private lateinit var adapter: RegionsSelectionAdapter

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi

    private val recyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val refreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val empty: View by bindView(R.id.empty)
    private val error: View by bindView(R.id.error)


    companion object {
        private const val TAG = "SetRegionActivity"

        fun getLaunchIntent(context: Context) = Intent(context, SetRegionActivity::class.java)
    }

    override val activityName = TAG

    override fun failure(errorCode: Int) {
        _selectedRegion = null
        regionsBundle = null
        showError()
    }

    private fun fetchRegionsBundle() {
        _selectedRegion = null
        regionsBundle = null
        refreshLayout.isRefreshing = true
        serverApi.getRegions(listener = ApiCall(this))
    }

    override fun navigateUp() {
        if (_selectedRegion == null) {
            super.navigateUp()
            return
        }

        AlertDialog.Builder(this)
                .setMessage(R.string.youve_selected_a_region_but_havent_saved)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes) { dialog, which ->
                    super@SetRegionActivity.navigateUp()
                }
                .show()
    }

    override fun onBackPressed() {
        if (_selectedRegion == null) {
            super.onBackPressed()
            return
        }

        AlertDialog.Builder(this)
                .setMessage(R.string.youve_selected_a_region_but_havent_saved)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes) { dialog, which ->
                    super@SetRegionActivity.onBackPressed()
                }
                .show()
    }

    override fun onClick(v: RegionSelectionItemView) {
        val region = v.region

        _selectedRegion = if (region == regionManager.getRegion()) null else region

        refreshMenu()
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_set_region)
        subtitle = regionManager.getRegion(this).displayName

        fetchRegionsBundle()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_set_region, menu)
        saveMenuItem = menu.findItem(R.id.miSave)
        refreshMenu()

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.miSave -> {
                save()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    override fun onRefresh() {
        fetchRegionsBundle()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.setHasFixedSize(true)
        adapter = RegionsSelectionAdapter(this)
        recyclerView.adapter = adapter
    }

    private fun refreshMenu() {
        val saveMenuItem = saveMenuItem ?: return

        if (refreshLayout.isRefreshing || adapter.isEmpty ||
                empty.visibility == View.VISIBLE || error.visibility == View.VISIBLE) {
            saveMenuItem.isEnabled = false
            saveMenuItem.isVisible = false
        } else {
            saveMenuItem.isEnabled = _selectedRegion != null
            saveMenuItem.isVisible = true
        }
    }

    private fun save() {
        regionManager.setRegion(_selectedRegion ?: throw RuntimeException("_selectedRegion is null"))
        setResult(Activity.RESULT_OK)
        supportFinishAfterTransition()
    }

    override val selectedRegion: Region?
        get() = _selectedRegion ?: regionManager.getRegion()

    private fun showEmpty() {
        adapter.clear()
        recyclerView.visibility = View.GONE
        error.visibility = View.GONE
        empty.visibility = View.VISIBLE
        refreshLayout.isRefreshing = false
        refreshMenu()
    }

    private fun showError() {
        adapter.clear()
        recyclerView.visibility = View.GONE
        empty.visibility = View.GONE
        error.visibility = View.VISIBLE
        refreshLayout.isRefreshing = false
        refreshMenu()
    }

    private fun showRegionsBundle() {
        adapter.set(ListUtils.createRegionsList(regionsBundle))
        empty.visibility = View.GONE
        error.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        refreshLayout.isRefreshing = false
        refreshLayout.isEnabled = false
        refreshMenu()
    }

    override val showUpNavigation = true

    override fun success(`object`: RegionsBundle?) {
        regionsBundle = `object`

        if (`object`?.regions?.isNotEmpty() == true) {
            showRegionsBundle()
        } else {
            showEmpty()
        }
    }

}

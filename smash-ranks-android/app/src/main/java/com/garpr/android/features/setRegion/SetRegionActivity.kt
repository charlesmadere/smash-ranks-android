package com.garpr.android.features.setRegion

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.activities.BaseActivity
import com.garpr.android.data.models.Region
import com.garpr.android.data.models.RegionsBundle
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.ListUtils
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.views.RegionSelectionItemView
import kotlinx.android.synthetic.main.activity_set_region.*
import javax.inject.Inject

class SetRegionActivity : BaseActivity(), ApiListener<RegionsBundle>,
        RegionSelectionItemView.Listeners, SetRegionToolbar.Listeners,
        SwipeRefreshLayout.OnRefreshListener {

    private var _selectedRegion: Region? = null
    private var regionsBundle: RegionsBundle? = null
    private val adapter = RegionsSelectionAdapter()

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi


    companion object {
        private const val TAG = "SetRegionActivity"

        fun getLaunchIntent(context: Context) = Intent(context, SetRegionActivity::class.java)
    }

    override val activityName = TAG

    override val enableSaveIcon: Boolean
        get() = showSaveIcon && _selectedRegion != null

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
        toolbar.refresh()
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(R.layout.activity_set_region)
        toolbar.subtitleText = regionManager.getRegion(this).displayName

        fetchRegionsBundle()
    }

    override fun onRefresh() {
        fetchRegionsBundle()
    }

    override fun onSaveClick(v: SetRegionToolbar) {
        regionManager.setRegion(_selectedRegion ?: throw RuntimeException("_selectedRegion is null"))
        setResult(Activity.RESULT_OK)
        supportFinishAfterTransition()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override val selectedRegion: Region?
        get() = _selectedRegion ?: regionManager.getRegion()

    private fun showEmpty() {
        adapter.clear()
        recyclerView.visibility = View.GONE
        error.visibility = View.GONE
        empty.visibility = View.VISIBLE
        refreshLayout.isRefreshing = false
        toolbar.refresh()
    }

    private fun showError() {
        adapter.clear()
        recyclerView.visibility = View.GONE
        empty.visibility = View.GONE
        error.visibility = View.VISIBLE
        refreshLayout.isRefreshing = false
        toolbar.refresh()
    }

    private fun showRegionsBundle() {
        adapter.set(ListUtils.createRegionsList(regionsBundle))
        empty.visibility = View.GONE
        error.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        refreshLayout.isRefreshing = false
        refreshLayout.isEnabled = false
        toolbar.refresh()
    }

    override val showSaveIcon: Boolean
        get() = !refreshLayout.isRefreshing && !adapter.isEmpty &&
                empty.visibility != View.VISIBLE && error.visibility != View.VISIBLE &&
                recyclerView.visibility == View.VISIBLE

    override fun success(`object`: RegionsBundle?) {
        regionsBundle = `object`

        if (`object`?.regions?.isNotEmpty() == true) {
            showRegionsBundle()
        } else {
            showEmpty()
        }
    }

}

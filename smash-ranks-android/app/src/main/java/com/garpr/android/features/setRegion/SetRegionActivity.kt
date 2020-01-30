package com.garpr.android.features.setRegion

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.features.common.views.RegionSelectionItemView
import com.garpr.android.features.setRegion.SetRegionViewModel.ListItem
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.RegionHandleUtils
import kotlinx.android.synthetic.main.activity_set_region.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SetRegionActivity : BaseActivity(), Refreshable, RegionSelectionItemView.Listener,
        SetRegionToolbar.Listener, SwipeRefreshLayout.OnRefreshListener {

    private val adapter = Adapter(this)

    private val viewModel: SetRegionViewModel by viewModel()

    protected val regionHandleUtils: RegionHandleUtils by inject()

    companion object {
        private const val TAG = "SetRegionActivity"

        fun getLaunchIntent(context: Context) = Intent(context, SetRegionActivity::class.java)
    }

    override val activityName = TAG

    private fun fetchRegions() {
        viewModel.fetchRegions()
    }

    private fun initListeners() {
        viewModel.stateLiveData.observe(this, Observer {
            refreshState(it)
        })
    }

    override fun navigateUp() {
        if (!viewModel.warnBeforeClose) {
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
        if (!viewModel.warnBeforeClose) {
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
        viewModel.selectedRegion = v.region
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_region)
        initListeners()
        fetchRegions()
    }

    override fun onRefresh() {
        refresh()
    }

    override fun onSaveClick(v: SetRegionToolbar) {
        viewModel.saveSelectedRegion()
        setResult(Activity.RESULT_OK)
        supportFinishAfterTransition()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        val region = regionHandleUtils.getRegion(this)
        toolbar.subtitleText = getString(R.string.region_endpoint_format, region.displayName,
                getText(region.endpoint.title))
        toolbar.listener = this

        refreshLayout.setOnRefreshListener(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun refresh() {
        fetchRegions()
    }

    private fun refreshState(state: SetRegionViewModel.State) {
        when (state.saveIconStatus) {
            SetRegionViewModel.SaveIconStatus.DISABLED -> {
                toolbar.showSaveIcon = true
                toolbar.enableSaveIcon = false
            }

            SetRegionViewModel.SaveIconStatus.ENABLED -> {
                toolbar.showSaveIcon = true
                toolbar.enableSaveIcon = true
            }

            SetRegionViewModel.SaveIconStatus.GONE -> {
                toolbar.showSaveIcon = false
                toolbar.enableSaveIcon = false
            }
        }

        if (state.hasError) {
            adapter.clear()
            recyclerView.visibility = View.GONE
            error.visibility = View.VISIBLE
        } else {
            adapter.set(state.list, state.selectedRegion)
            error.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        if (state.isFetching) {
            refreshLayout.isEnabled = true
            refreshLayout.isRefreshing = true
        } else {
            refreshLayout.isRefreshing = false
            refreshLayout.isEnabled = state.isRefreshEnabled
        }
    }

    private class Adapter(
            private val regionItemViewListener: RegionSelectionItemView.Listener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val list = mutableListOf<ListItem>()
        private var selectedRegion: Region? = null

        init {
            setHasStableIds(true)
        }

        private fun bindEndpoint(holder: EndpointViewHolder, item: ListItem.Endpoint) {
            holder.endpointDividerView.setContent(item.endpoint)
        }

        private fun bindEndpointError(holder: EndpointErrorViewHolder, item: ListItem.EndpointError) {
            holder.endpointErrorItemView.text = holder.endpointErrorItemView.context.getString(
                    R.string.error_loading_regions_from_x,
                    holder.endpointErrorItemView.context.getText(item.endpoint.title)
            )
        }

        private fun bindRegion(holder: RegionViewHolder, item: ListItem.Region) {
            holder.regionSelectionItemView.setContent(item.region, item.region == selectedRegion)
        }

        internal fun clear() {
            list.clear()
            selectedRegion = null
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun getItemId(position: Int): Long {
            return list[position].listId
        }

        override fun getItemViewType(position: Int): Int {
            return when (list[position]) {
                is ListItem.Endpoint -> VIEW_TYPE_ENDPOINT
                is ListItem.EndpointError -> VIEW_TYPE_ENDPOINT_ERROR
                is ListItem.Region -> VIEW_TYPE_REGION
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (val item = list[position]) {
                is ListItem.Endpoint -> bindEndpoint(holder as EndpointViewHolder, item)
                is ListItem.EndpointError -> bindEndpointError(holder as EndpointErrorViewHolder, item)
                is ListItem.Region -> bindRegion(holder as RegionViewHolder, item)
                else -> throw RuntimeException("unknown item: $item, position: $position")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = parent.layoutInflater

            return when (viewType) {
                VIEW_TYPE_ENDPOINT -> EndpointViewHolder(inflater.inflate(
                        R.layout.divider_endpoint, parent, false))
                VIEW_TYPE_ENDPOINT_ERROR -> EndpointErrorViewHolder(inflater.inflate(
                        R.layout.item_string_with_background, parent, false))
                VIEW_TYPE_REGION -> RegionViewHolder(regionItemViewListener,
                        inflater.inflate(R.layout.item_region_selection, parent, false))
                else -> throw IllegalArgumentException("unknown viewType: $viewType")
            }
        }

        internal fun set(list: List<ListItem>?, selectedRegion: Region?) {
            this.list.clear()

            if (!list.isNullOrEmpty()) {
                this.list.addAll(list)
            }

            this.selectedRegion = selectedRegion
            notifyDataSetChanged()
        }

        companion object {
            private const val VIEW_TYPE_ENDPOINT = 0
            private const val VIEW_TYPE_ENDPOINT_ERROR = 1
            private const val VIEW_TYPE_REGION = 2
        }

    }

    private class EndpointErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val endpointErrorItemView: TextView = itemView as TextView
    }

    private class EndpointViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val endpointDividerView: EndpointDividerView = itemView as EndpointDividerView
    }

    private class RegionViewHolder(
            listener: RegionSelectionItemView.Listener,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        internal val regionSelectionItemView: RegionSelectionItemView = itemView as RegionSelectionItemView

        init {
            regionSelectionItemView.listener = listener
        }
    }

}

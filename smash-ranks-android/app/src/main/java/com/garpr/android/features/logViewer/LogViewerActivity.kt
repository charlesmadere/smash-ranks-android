package com.garpr.android.features.logViewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.viewModel
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Timber
import kotlinx.android.synthetic.main.activity_log_viewer.*

class LogViewerActivity : BaseActivity(), LogViewerToolbar.Listener, Refreshable,
        SwipeRefreshLayout.OnRefreshListener {

    private val adapter = Adapter()
    private val viewModel by viewModel(this) { appComponent.logViewerViewModel }


    companion object {
        private const val TAG = "LogViewerActivity"

        fun getLaunchIntent(context: Context) = Intent(context, LogViewerActivity::class.java)
    }

    override val activityName = TAG

    private fun initListeners() {
        viewModel.stateLiveData.observe(this, Observer {
            refreshState(it)
        })
    }

    override fun onClearClick(v: LogViewerToolbar) {
        viewModel.clearEntries()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_viewer)
        initListeners()
        refresh()
    }

    override fun onRefresh() {
        refresh()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        toolbar.listener = this
    }

    override fun refresh() {
        viewModel.fetchEntries()
    }

    private fun refreshState(state: LogViewerViewModel.State) {
        if (state.isEmpty) {
            adapter.clear()
            recyclerView.visibility = View.GONE
            empty.visibility = View.VISIBLE
            toolbar.isClearEnabled = false
        } else {
            adapter.set(state.entries)
            empty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            toolbar.isClearEnabled = true
        }

        refreshLayout.isRefreshing = state.isFetching
    }

    private class Adapter : RecyclerView.Adapter<TimberEntryViewHolder>() {

        private val list = mutableListOf<Timber.Entry>()

        internal fun clear() {
            list.clear()
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: TimberEntryViewHolder, position: Int) {
            holder.timberEntryItemView.setContent(list[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimberEntryViewHolder {
            val inflater = parent.layoutInflater
            return TimberEntryViewHolder(inflater.inflate(R.layout.item_timber_entry, parent,
                    false))
        }

        internal fun set(list: List<Timber.Entry>?) {
            this.list.clear()

            if (!list.isNullOrEmpty()) {
                this.list.addAll(list)
            }

            notifyDataSetChanged()
        }

    }

    private class TimberEntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val timberEntryItemView: TimberEntryItemView = itemView as TimberEntryItemView
    }

}

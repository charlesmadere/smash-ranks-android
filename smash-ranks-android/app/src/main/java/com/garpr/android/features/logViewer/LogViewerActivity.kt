package com.garpr.android.features.logViewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.features.base.BaseActivity
import kotlinx.android.synthetic.main.activity_log_viewer.*

class LogViewerActivity : BaseActivity(), LogViewerToolbar.Listeners,
        SwipeRefreshLayout.OnRefreshListener {

    private val adapter = TimberEntriesAdapter()


    companion object {
        private const val TAG = "LogViewerActivity"

        fun getLaunchIntent(context: Context) = Intent(context, LogViewerActivity::class.java)
    }

    override val activityName = TAG

    override val enableClearButton: Boolean
        get() = !refreshLayout.isRefreshing && !adapter.isEmpty

    private fun fetchTimberEntries() {
        refreshLayout.isRefreshing = true
        adapter.set(timber.entries)

        if (adapter.isEmpty) {
            recyclerView.visibility = View.GONE
            empty.visibility = View.VISIBLE
        } else {
            empty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        refreshLayout.isRefreshing = false
        toolbar.refresh()
    }

    override fun onClearClick(v: LogViewerToolbar) {
        timber.clearEntries()
        fetchTimberEntries()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_viewer)
        fetchTimberEntries()
    }

    override fun onRefresh() {
        fetchTimberEntries()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

}

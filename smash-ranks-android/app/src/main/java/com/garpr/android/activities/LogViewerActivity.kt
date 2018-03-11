package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.garpr.android.R
import com.garpr.android.adapters.TimberEntriesAdapter
import kotterknife.bindView

class LogViewerActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: TimberEntriesAdapter

    private val recyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val refreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val empty: View by bindView(R.id.empty)


    companion object {
        private const val TAG = "LogViewerActivity"

        fun getLaunchIntent(context: Context) = Intent(context, LogViewerActivity::class.java)
    }

    override val activityName = TAG

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

        invalidateOptionsMenu()
        refreshLayout.isRefreshing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_viewer)
        fetchTimberEntries()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_log_viewer, menu)
        menu.findItem(R.id.miClearLog).isEnabled = !adapter.isEmpty
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.miClearLog -> {
                timber.clearEntries()
                fetchTimberEntries()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    override fun onRefresh() {
        fetchTimberEntries()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        adapter = TimberEntriesAdapter(this)
        recyclerView.adapter = adapter
    }

    override val showUpNavigation = true

}

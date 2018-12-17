package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.adapters.TimberEntriesAdapter
import kotlinx.android.synthetic.main.activity_log_viewer.*

class LogViewerActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: TimberEntriesAdapter

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
        recyclerView.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        adapter = TimberEntriesAdapter(this)
        recyclerView.adapter = adapter
    }

}

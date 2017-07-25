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

    lateinit private var mAdapter: TimberEntriesAdapter

    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val mRefreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val mEmpty: View by bindView(R.id.empty)


    companion object {
        private const val TAG = "LogViewerActivity"

        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, LogViewerActivity::class.java)
        }
    }

    override val activityName = TAG

    private fun fetchTimberEntries() {
        mRefreshLayout.isRefreshing = true
        mAdapter.set(mTimber.entries)

        if (mAdapter.isEmpty) {
            mRecyclerView.visibility = View.GONE
            mEmpty.visibility = View.VISIBLE
        } else {
            mEmpty.visibility = View.GONE
            mRecyclerView.visibility = View.VISIBLE
        }

        supportInvalidateOptionsMenu()
        mRefreshLayout.isRefreshing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_viewer)
        fetchTimberEntries()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_log_viewer, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miClearLog -> {
                mTimber.clearEntries()
                fetchTimberEntries()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (!mAdapter.isEmpty) {
            menu.findItem(R.id.miClearLog).isEnabled = true
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onRefresh() {
        fetchTimberEntries()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        mRefreshLayout.setOnRefreshListener(this)
        mRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mRecyclerView.setHasFixedSize(true)
        mAdapter = TimberEntriesAdapter(this)
        mRecyclerView.adapter = mAdapter
    }

    override val showUpNavigation = true

}

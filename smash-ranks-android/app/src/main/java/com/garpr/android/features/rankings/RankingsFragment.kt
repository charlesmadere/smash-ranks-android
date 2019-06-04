package com.garpr.android.features.rankings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.features.common.fragments.BaseFragment
import com.garpr.android.features.notifications.NotificationsManager
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.networking.ServerApi
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.fragment_rankings.*
import javax.inject.Inject

class RankingsFragment : BaseFragment(), Refreshable, Searchable, SearchQueryHandle,
        SwipeRefreshLayout.OnRefreshListener {

    private val adapter = RankingsAdapter()

    @Inject
    protected lateinit var notificationsManager: NotificationsManager

    @Inject
    protected lateinit var regionRepository: RegionRepository

    @Inject
    protected lateinit var serverApi: ServerApi


    companion object {
        fun create() = RankingsFragment()
    }

    private fun fetchRankingsBundle() {
        refreshLayout.isRefreshing = true

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_rankings, container, false)
    }

    override fun onRefresh() {
        fetchRankingsBundle()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout.setOnRefreshListener(this)
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        fetchRankingsBundle()
    }

    override fun refresh() {
        notificationsManager.cancelAll()
        fetchRankingsBundle()
    }

    override fun search(query: String?) {

    }

    override val searchQuery: CharSequence?
        get() = (activity as? SearchQueryHandle?)?.searchQuery

    private fun showEmpty() {

    }

    private fun showError() {

    }

    private fun showRankingsBundle() {

    }

}

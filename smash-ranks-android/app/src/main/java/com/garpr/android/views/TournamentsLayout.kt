package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.TournamentsAdapter
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.models.AbsTournament
import com.garpr.android.models.TournamentsBundle
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import kotterknife.bindView
import javax.inject.Inject

class TournamentsLayout : SearchableFrameLayout, ApiListener<TournamentsBundle>, Refreshable,
        SwipeRefreshLayout.OnRefreshListener {

    private lateinit var mAdapter: TournamentsAdapter

    @Inject
    protected lateinit var mRegionManager: RegionManager

    @Inject
    protected lateinit var mServerApi: ServerApi

    private val mError: ErrorContentLinearLayout by bindView(R.id.error)
    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val mRefreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val mEmpty: View by bindView(R.id.empty)


    companion object {
        fun inflate(parent: ViewGroup): TournamentsLayout {
            val inflater = LayoutInflater.from(parent.context)
            return inflater.inflate(R.layout.layout_tournaments, parent, false) as TournamentsLayout
        }
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun failure(errorCode: Int) {
        tournamentsBundle = null
        showError(errorCode)
    }

    private fun fetchTournamentsBundle() {
        mRefreshLayout.isRefreshing = true
        mServerApi.getTournaments(mRegionManager.getRegion(context), ApiCall(this))
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        App.get().appComponent.inject(this)

        mRefreshLayout.setOnRefreshListener(this)
        mRecyclerView.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
        mRecyclerView.setHasFixedSize(true)
        mAdapter = TournamentsAdapter(context)
        mRecyclerView.adapter = mAdapter

        fetchTournamentsBundle()
    }

    override fun onRefresh() {
        fetchTournamentsBundle()
    }

    override val recyclerView: RecyclerView?
        get() = mRecyclerView

    override fun refresh() {
        fetchTournamentsBundle()
    }

    override fun search(query: String?) {
        val tournaments = tournamentsBundle?.tournaments

        if (tournaments == null || tournaments.isEmpty()) {
            return
        }

        mThreadUtils.run(object : ThreadUtils.Task {
            private var mList: List<AbsTournament>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mList = ListUtils.searchTournamentList(query, tournaments)
            }

            override fun onUi() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mAdapter.set(mList)
            }
        })
    }

    private fun showEmpty() {
        mAdapter.clear()
        mRecyclerView.visibility = View.GONE
        mError.visibility = View.GONE
        mEmpty.visibility = View.VISIBLE
        mRefreshLayout.isRefreshing = false
    }

    private fun showError(errorCode: Int) {
        mAdapter.clear()
        mRecyclerView.visibility = View.GONE
        mEmpty.visibility = View.GONE
        mError.setVisibility(View.VISIBLE, errorCode)
        mRefreshLayout.isRefreshing = false
    }

    private fun showTournamentsBundle() {
        mAdapter.set(ListUtils.createTournamentList(tournamentsBundle))
        mEmpty.visibility = View.GONE
        mError.visibility = View.GONE
        mRecyclerView.visibility = View.VISIBLE
        mRefreshLayout.isRefreshing = false
    }

    override fun success(`object`: TournamentsBundle?) {
        tournamentsBundle = `object`

        if (`object`?.tournaments?.isNotEmpty() == true) {
            showTournamentsBundle()
        } else {
            showEmpty()
        }
    }

    var tournamentsBundle: TournamentsBundle? = null
        private set

}

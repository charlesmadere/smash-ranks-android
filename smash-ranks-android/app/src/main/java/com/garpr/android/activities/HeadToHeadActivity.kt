package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import com.garpr.android.R
import com.garpr.android.adapters.HeadToHeadAdapter
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.extensions.requireStringExtra
import com.garpr.android.extensions.subtitle
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.models.*
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.views.ErrorContentLinearLayout
import kotterknife.bindView
import javax.inject.Inject

class HeadToHeadActivity : BaseActivity(), ApiListener<HeadToHead>,
        SwipeRefreshLayout.OnRefreshListener {

    private var list: List<Any>? = null
    private var headToHead: HeadToHead? = null
    private lateinit var adapter: HeadToHeadAdapter

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi

    @Inject
    protected lateinit var threadUtils: ThreadUtils

    private val error: ErrorContentLinearLayout by bindView(R.id.error)
    private val recyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val refreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val empty: View by bindView(R.id.empty)


    companion object {
        private const val TAG = "HeadToHeadActivity"
        private val CNAME = HeadToHeadActivity::class.java.canonicalName
        private val EXTRA_PLAYER_ID = "$CNAME.PlayerId"
        private val EXTRA_OPPONENT_ID = "$CNAME.OpponentId"

        fun getLaunchIntent(context: Context, player: AbsPlayer, opponent: AbsPlayer,
                region: Region? = null): Intent {
            var regionCopy = region

            if (player is FavoritePlayer) {
                regionCopy = player.region
            }

            return getLaunchIntent(context, player.id, opponent.id, regionCopy)
        }

        fun getLaunchIntent(context: Context, player: AbsPlayer, match: Match,
                region: Region? = null): Intent {
            var regionCopy = region

            if (player is FavoritePlayer) {
                regionCopy = player.region
            }

            return getLaunchIntent(context, player.id, match.opponent.id, regionCopy)
        }

        fun getLaunchIntent(context: Context, match: FullTournament.Match,
                region: Region? = null): Intent {
            return getLaunchIntent(context, match.winnerId, match.loserId, region)
        }

        fun getLaunchIntent(context: Context, playerId: String, opponentId: String,
                region: Region? = null): Intent {
            return Intent(context, HeadToHeadActivity::class.java)
                    .putExtra(EXTRA_PLAYER_ID, playerId)
                    .putExtra(EXTRA_OPPONENT_ID, opponentId)
                    .putOptionalExtra(EXTRA_REGION, region)
        }
    }

    override val activityName = TAG

    override fun failure(errorCode: Int) {
        headToHead = null
        list = null
        showError(errorCode)
    }

    private fun fetchHeadToHead() {
        refreshLayout.isRefreshing = true
        serverApi.getHeadToHead(regionManager.getRegion(this), playerId, opponentId,
                ApiCall(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(R.layout.activity_head_to_head)
        subtitle = regionManager.getRegion(this).displayName

        fetchHeadToHead()
    }

    private val opponentId: String by lazy { intent.requireStringExtra(EXTRA_OPPONENT_ID) }

    override fun onRefresh() {
        fetchHeadToHead()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.setHasFixedSize(true)
        adapter = HeadToHeadAdapter(this)
        recyclerView.adapter = adapter
    }

    private val playerId: String by lazy { intent.requireStringExtra(EXTRA_PLAYER_ID) }

    private fun showData() {
        list = ListUtils.createHeadToHeadList(this, headToHead)
        adapter.set(list)

        if (adapter.isEmpty) {
            error.visibility = View.GONE
            recyclerView.visibility = View.GONE
            empty.visibility = View.VISIBLE
        } else {
            empty.visibility = View.GONE
            error.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        refreshLayout.isRefreshing = false
        invalidateOptionsMenu()
    }

    private fun showError(errorCode: Int) {
        adapter.clear()
        empty.visibility = View.GONE
        recyclerView.visibility = View.GONE
        error.setVisibility(View.VISIBLE, errorCode)
        invalidateOptionsMenu()
        refreshLayout.isRefreshing = false
    }

    override val showUpNavigation = true

    override fun success(`object`: HeadToHead?) {
        headToHead = `object`
        list = null
        showData()
    }

}

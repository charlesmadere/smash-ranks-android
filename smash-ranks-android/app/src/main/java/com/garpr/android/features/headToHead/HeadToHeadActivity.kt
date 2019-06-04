package com.garpr.android.features.headToHead

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.HeadToHead
import com.garpr.android.data.models.Match
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.extensions.requireStringExtra
import com.garpr.android.features.common.activities.BaseActivity
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.activity_head_to_head.*
import javax.inject.Inject

class HeadToHeadActivity : BaseActivity(), ApiListener<HeadToHead>,
        SwipeRefreshLayout.OnRefreshListener {

    private var headToHead: HeadToHead? = null
    private val adapter = HeadToHeadAdapter()
    private var list: List<Any>? = null

    private val opponentId: String by lazy { intent.requireStringExtra(EXTRA_OPPONENT_ID) }
    private val playerId: String by lazy { intent.requireStringExtra(EXTRA_PLAYER_ID) }

    @Inject
    protected lateinit var regionRepository: RegionRepository

    @Inject
    protected lateinit var serverApi: ServerApi

    @Inject
    protected lateinit var threadUtils: ThreadUtils


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
        serverApi.getHeadToHead(regionRepository.getRegion(this), playerId, opponentId,
                ApiCall(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(R.layout.activity_head_to_head)
        toolbar.subtitleText = regionRepository.getRegion(this).displayName

        fetchHeadToHead()
    }

    override fun onRefresh() {
        fetchHeadToHead()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

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
        toolbar.refresh()
    }

    private fun showError(errorCode: Int) {
        adapter.clear()
        empty.visibility = View.GONE
        recyclerView.visibility = View.GONE
        error.setVisibility(View.VISIBLE, errorCode)
        refreshLayout.isRefreshing = false
        toolbar.refresh()
    }

    override fun success(`object`: HeadToHead?) {
        headToHead = `object`
        list = null
        showData()
    }

}

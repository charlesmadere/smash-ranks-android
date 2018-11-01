package com.garpr.android.views

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.adapters.PlayersAdapter
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.PlayersBundle
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import kotterknife.bindView
import javax.inject.Inject

class PlayersLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SearchableRefreshLayout(context, attrs), ApiListener<PlayersBundle>, Refreshable,
        SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: PlayersAdapter

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi

    private val error: ErrorContentLinearLayout by bindView(R.id.error)
    private val empty: View by bindView(R.id.empty)


    interface Listener {
        fun onPlayersBundleFetched(layout: PlayersLayout)
    }

    override fun failure(errorCode: Int) {
        playersBundle = null
        showError(errorCode)
    }

    private fun fetchPlayersBundle() {
        isRefreshing = true
        serverApi.getPlayers(regionManager.getRegion(context), ApiCall(this))
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        appComponent.inject(this)

        setOnRefreshListener(this)
        recyclerView.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        adapter = PlayersAdapter(context)
        recyclerView.adapter = adapter

        fetchPlayersBundle()
    }

    private fun onPlayersBundleFetched() {
        if (!isAlive) {
            return
        }

        (context.activity as? Listener)?.onPlayersBundleFetched(this)
    }

    override fun onRefresh() {
        fetchPlayersBundle()
    }

    var playersBundle: PlayersBundle? = null
        private set

    override val recyclerView: RecyclerView by bindView(R.id.recyclerView)

    override fun refresh() {
        fetchPlayersBundle()
    }

    override fun search(query: String?) {
        val players = playersBundle?.players

        if (players.isNullOrEmpty()) {
            return
        }

        threadUtils.run(object : ThreadUtils.Task {
            private var list: List<AbsPlayer>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                list = ListUtils.searchPlayerList(query, players)
            }

            override fun onUi() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                adapter.set(list)
            }
        })
    }

    private fun showEmpty() {
        adapter.clear()
        recyclerView.visibility = View.GONE
        error.visibility = View.GONE
        empty.visibility = View.VISIBLE
        isRefreshing = false
    }

    private fun showError(errorCode: Int) {
        adapter.clear()
        recyclerView.visibility = View.GONE
        empty.visibility = View.GONE
        error.setVisibility(View.VISIBLE, errorCode)
        isRefreshing = false
    }

    private fun showPlayersBundle() {
        adapter.set(playersBundle)
        empty.visibility = View.GONE
        error.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        isRefreshing = false
    }

    override fun success(`object`: PlayersBundle?) {
        playersBundle = `object`

        if (`object`?.players?.isNotEmpty() == true) {
            showPlayersBundle()
        } else {
            showEmpty()
        }

        onPlayersBundleFetched()
    }

}

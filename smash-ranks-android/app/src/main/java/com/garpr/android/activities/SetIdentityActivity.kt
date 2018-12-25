package com.garpr.android.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.garpr.android.R
import com.garpr.android.adapters.PlayersSelectionAdapter
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.IdentityManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.PlayersBundle
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.views.PlayerSelectionItemView
import com.garpr.android.views.toolbars.SearchToolbar
import com.garpr.android.views.toolbars.SetIdentityToolbar
import kotlinx.android.synthetic.main.activity_set_identity.*
import javax.inject.Inject

class SetIdentityActivity : BaseActivity(), ApiListener<PlayersBundle>,
        PlayerSelectionItemView.Listeners, Searchable, SearchQueryHandle, SearchToolbar.Listener,
        SetIdentityToolbar.Listeners, SwipeRefreshLayout.OnRefreshListener {

    private var _selectedPlayer: AbsPlayer? = null
    private var playersBundle: PlayersBundle? = null
    private lateinit var adapter: PlayersSelectionAdapter

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi

    @Inject
    protected lateinit var threadUtils: ThreadUtils


    companion object {
        private const val TAG = "SetIdentityActivity"

        fun getLaunchIntent(context: Context) = Intent(context, SetIdentityActivity::class.java)
    }

    override val activityName = TAG

    override val enableSaveIcon: Boolean
        get() = showSaveIcon && _selectedPlayer != null

    override fun failure(errorCode: Int) {
        _selectedPlayer = null
        playersBundle = null
        showError()
    }

    private fun fetchPlayersBundle() {
        refreshLayout.isRefreshing = true
        serverApi.getPlayers(regionManager.getRegion(this), ApiCall(this))
    }

    override fun navigateUp() {
        if (_selectedPlayer == null) {
            super.navigateUp()
            return
        }

        AlertDialog.Builder(this)
                .setMessage(R.string.youve_selected_an_identity_but_havent_saved)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes) { dialog, which ->
                    super@SetIdentityActivity.navigateUp()
                }
                .show()
    }

    override fun onBackPressed() {
        if (toolbar.isSearchFieldExpanded) {
            toolbar.closeSearchField()
            return
        }

        if (_selectedPlayer == null) {
            super.onBackPressed()
            return
        }

        AlertDialog.Builder(this)
                .setMessage(R.string.youve_selected_an_identity_but_havent_saved)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes) { dialog, which ->
                    super@SetIdentityActivity.onBackPressed()
                }
                .show()
    }

    override fun onClick(v: PlayerSelectionItemView) {
        val player = v.player

        _selectedPlayer = if (player == identityManager.identity) {
            null
        } else {
            player
        }

        toolbar.refresh()
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(R.layout.activity_set_identity)
        toolbar.subtitleText = regionManager.getRegion(this).displayName

        fetchPlayersBundle()
    }

    override fun onRefresh() {
        fetchPlayersBundle()
    }

    override fun onSaveClick(v: SetIdentityToolbar) {
        val selectedPlayer = _selectedPlayer ?: throw NullPointerException("_selectedPlayer is null")
        identityManager.setIdentity(selectedPlayer, regionManager.getRegion(this))
        setResult(Activity.RESULT_OK)
        supportFinishAfterTransition()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        adapter = PlayersSelectionAdapter(this)
        recyclerView.adapter = adapter
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

    override val searchQuery: CharSequence?
        get() = toolbar.searchQuery

    override val selectedPlayer: AbsPlayer?
        get() = _selectedPlayer ?: identityManager.identity

    private fun showEmpty() {
        adapter.clear()
        recyclerView.visibility = View.GONE
        error.visibility = View.GONE
        empty.visibility = View.VISIBLE
        refreshLayout.isRefreshing = false
        toolbar.refresh()
    }

    private fun showError() {
        adapter.clear()
        recyclerView.visibility = View.GONE
        empty.visibility = View.GONE
        error.visibility = View.VISIBLE
        refreshLayout.isRefreshing = false
        toolbar.refresh()
    }

    private fun showPlayersBundle() {
        adapter.set(playersBundle)
        empty.visibility = View.GONE
        error.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        refreshLayout.isRefreshing = false
        refreshLayout.isEnabled = false
        toolbar.refresh()
    }

    override val showSaveIcon: Boolean
        get() = !refreshLayout.isRefreshing && empty.visibility != View.VISIBLE &&
                error.visibility != View.VISIBLE && recyclerView.visibility == View.VISIBLE

    override val showSearchIcon: Boolean
        get() = showSaveIcon

    override fun success(`object`: PlayersBundle?) {
        _selectedPlayer = null
        playersBundle = `object`

        if (`object` != null && `object`.players?.isNotEmpty() == true) {
            showPlayersBundle()
        } else {
            showEmpty()
        }
    }

}

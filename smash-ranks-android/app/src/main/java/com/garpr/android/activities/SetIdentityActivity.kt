package com.garpr.android.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.PlayersSelectionAdapter
import com.garpr.android.extensions.subtitle
import com.garpr.android.misc.*
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.PlayersBundle
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.views.PlayerSelectionItemView
import kotterknife.bindView
import javax.inject.Inject

class SetIdentityActivity : BaseActivity(), ApiListener<PlayersBundle>,
        MenuItem.OnActionExpandListener, PlayerSelectionItemView.Listeners, SearchQueryHandle,
        SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    private var _selectedPlayer: AbsPlayer? = null
    private var saveMenuItem: MenuItem? = null
    private var searchMenuItem: MenuItem? = null
    private var playersBundle: PlayersBundle? = null
    private lateinit var adapter: PlayersSelectionAdapter
    private var searchView: SearchView? = null

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var serverApi: ServerApi

    @Inject
    protected lateinit var threadUtils: ThreadUtils

    private val recyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val refreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val empty: View by bindView(R.id.empty)
    private val error: View by bindView(R.id.error)


    companion object {
        private const val TAG = "SetIdentityActivity"

        fun getLaunchIntent(context: Context) = Intent(context, SetIdentityActivity::class.java)
    }

    override val activityName = TAG

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
        if (selectedPlayer == null) {
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
        if (selectedPlayer == null) {
            super.onBackPressed()
            return
        }

        if (searchMenuItem?.isActionViewExpanded == true) {
            searchMenuItem?.collapseActionView()
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

        refreshMenu()
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_set_identity)
        subtitle = regionManager.getRegion(this).displayName

        fetchPlayersBundle()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_set_identity, menu)

        val searchMenuItem = menu.findItem(R.id.miSearch) ?: throw RuntimeException(
                "searchMenuItem is null")
        searchMenuItem.setOnActionExpandListener(this)
        this.searchMenuItem = searchMenuItem

        val searchView = searchMenuItem.actionView as SearchView
        searchView.queryHint = getText(R.string.search_)
        searchView.setOnQueryTextListener(this)
        this.searchView = searchView

        saveMenuItem = menu.findItem(R.id.miSave)
        refreshMenu()

        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        search(null)
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem) = true

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.miSave -> {
                save()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    override fun onQueryTextChange(newText: String): Boolean {
        search(newText)
        return false
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        search(query)
        return false
    }

    override fun onRefresh() {
        fetchPlayersBundle()
    }

    override fun onViewsBound() {
        super.onViewsBound()

        refreshLayout.setOnRefreshListener(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        adapter = PlayersSelectionAdapter(this)
        recyclerView.adapter = adapter
    }

    private fun refreshMenu() {
        val saveMenuItem = this.saveMenuItem
        val searchMenuItem = this.searchMenuItem
        val searchView = this.searchView

        if (saveMenuItem == null || searchMenuItem == null || searchView == null) {
            return
        }

        if (refreshLayout.isRefreshing || adapter.isEmpty || empty.visibility == View.VISIBLE
                || error.visibility == View.VISIBLE) {
            saveMenuItem.isEnabled = false
            saveMenuItem.isVisible = false
            searchMenuItem.collapseActionView()
            searchMenuItem.isVisible = false
        } else {
            saveMenuItem.isEnabled = selectedPlayer != null
            saveMenuItem.isVisible = true
            searchMenuItem.isVisible = true
        }
    }

    private fun save() {
        val selectedPlayer = selectedPlayer ?: throw NullPointerException("selectedPlayer is null")
        identityManager.setIdentity(selectedPlayer, regionManager.getRegion(this))
        setResult(Activity.RESULT_OK)
        supportFinishAfterTransition()
    }

    private fun search(query: String?) {
        val players = playersBundle?.players

        if (players == null || players.isEmpty()) {
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
        get() = searchView?.query

    override val selectedPlayer: AbsPlayer?
        get() = _selectedPlayer ?: identityManager.identity

    private fun showEmpty() {
        adapter.clear()
        recyclerView.visibility = View.GONE
        error.visibility = View.GONE
        empty.visibility = View.VISIBLE
        refreshLayout.isRefreshing = false
        refreshMenu()
    }

    private fun showError() {
        adapter.clear()
        recyclerView.visibility = View.GONE
        empty.visibility = View.GONE
        error.visibility = View.VISIBLE
        refreshLayout.isRefreshing = false
        refreshMenu()
    }

    private fun showPlayersBundle() {
        adapter.set(playersBundle)
        empty.visibility = View.GONE
        error.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        refreshLayout.isRefreshing = false
        refreshLayout.isEnabled = false
        refreshMenu()
    }

    override val showUpNavigation = true

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

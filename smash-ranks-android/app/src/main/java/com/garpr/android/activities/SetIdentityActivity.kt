package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.PlayersSelectionAdapter
import com.garpr.android.misc.IdentityManager
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.ResultCodes
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.PlayersBundle
import com.garpr.android.networking.ApiCall
import com.garpr.android.networking.ApiListener
import com.garpr.android.networking.ServerApi
import com.garpr.android.views.PlayerSelectionItemView
import kotterknife.bindView
import javax.inject.Inject

class SetIdentityActivity : BaseActivity(), ApiListener<PlayersBundle>,
        MenuItemCompat.OnActionExpandListener, PlayerSelectionItemView.Listeners,
        SearchQueryHandle, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    private var mSelectedPlayer: AbsPlayer? = null
    private var mSaveMenuItem: MenuItem? = null
    private var mSearchMenuItem: MenuItem? = null
    private var mPlayersBundle: PlayersBundle? = null
    lateinit private var mAdapter: PlayersSelectionAdapter
    private var mSearchView: SearchView? = null

    @Inject
    lateinit protected var mIdentityManager: IdentityManager

    @Inject
    lateinit protected var mRegionManager: RegionManager

    @Inject
    lateinit protected var mServerApi: ServerApi

    @Inject
    lateinit protected var mThreadUtils: ThreadUtils

    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val mRefreshLayout: SwipeRefreshLayout by bindView(R.id.refreshLayout)
    private val mEmpty: View by bindView(R.id.empty)
    private val mError: View by bindView(R.id.error)


    companion object {
        private val TAG = "SetIdentityActivity"

        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, SetIdentityActivity::class.java)
        }
    }

    override val activityName: String
        get() = TAG

    override fun failure(errorCode: Int) {
        mSelectedPlayer = null
        mPlayersBundle = null
        showError()
    }

    private fun fetchPlayersBundle() {
        mRefreshLayout.isRefreshing = true
        mServerApi.getPlayers(mRegionManager.getRegion(this), ApiCall(this))
    }

    override fun navigateUp() {
        if (mSelectedPlayer == null) {
            super.navigateUp()
            return
        }

        AlertDialog.Builder(this)
                .setMessage(R.string.youve_selected_an_identity_but_havent_saved)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes) { dialog, which ->
                    super@SetIdentityActivity.navigateUp() }
                .show()
    }

    override fun onBackPressed() {
        if (mSelectedPlayer == null) {
            super.onBackPressed()
            return
        }

        if (mSearchMenuItem != null && MenuItemCompat.isActionViewExpanded(mSearchMenuItem)) {
            MenuItemCompat.collapseActionView(mSearchMenuItem)
            return
        }

        AlertDialog.Builder(this)
                .setMessage(R.string.youve_selected_an_identity_but_havent_saved)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes) { dialog, which ->
                    super@SetIdentityActivity.onBackPressed() }
                .show()
    }

    override fun onClick(v: PlayerSelectionItemView) {
        val player = v.mContent

        if (player == mIdentityManager.identity) {
            mSelectedPlayer = null
        } else {
            mSelectedPlayer = player
        }

        refreshMenu()
        mAdapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_set_identity)
        subtitle = mRegionManager.getRegion(this).displayName

        fetchPlayersBundle()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_set_identity, menu)

        mSearchMenuItem = menu.findItem(R.id.miSearch)
        MenuItemCompat.setOnActionExpandListener(mSearchMenuItem, this)
        mSearchView = MenuItemCompat.getActionView(mSearchMenuItem) as SearchView
        mSearchView!!.queryHint = getText(R.string.search_players_)
        mSearchView!!.setOnQueryTextListener(this)

        mSaveMenuItem = menu.findItem(R.id.miSave)
        refreshMenu()

        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        search(null)
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miSave -> {
                save()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
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

        mRefreshLayout.setOnRefreshListener(this)
        mRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mRecyclerView.setHasFixedSize(true)
        mAdapter = PlayersSelectionAdapter(this)
        mRecyclerView.adapter = mAdapter
    }

    private fun refreshMenu() {
        val saveMenuItem = mSaveMenuItem
        val searchMenuItem = mSearchMenuItem
        val searchView = mSearchView

        if (saveMenuItem == null || searchMenuItem == null || searchView == null) {
            return
        }

        if (mRefreshLayout.isRefreshing || mAdapter.isEmpty ||
                mEmpty.visibility == View.VISIBLE || mError.visibility == View.VISIBLE) {
            saveMenuItem.isEnabled = false
            saveMenuItem.isVisible = false
            MenuItemCompat.collapseActionView(mSearchMenuItem)
            searchMenuItem.isVisible = false
        } else {
            saveMenuItem.isEnabled = mSelectedPlayer != null
            saveMenuItem.isVisible = true
            searchMenuItem.isVisible = true
        }
    }

    private fun save() {
        mIdentityManager.setIdentity(mSelectedPlayer!!, mRegionManager.getRegion(this))
        Toast.makeText(this, R.string.identity_saved_, Toast.LENGTH_LONG).show()
        setResult(ResultCodes.IDENTITY_SELECTED.mValue)
        supportFinishAfterTransition()
    }

    private fun search(query: String?) {
        val playersBundle = mPlayersBundle

        if (playersBundle == null || !playersBundle.hasPlayers()) {
            return
        }

        mThreadUtils.run(object : ThreadUtils.Task {
            private var mList: List<AbsPlayer>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mList = ListUtils.searchPlayerList(query, playersBundle.players)
            }

            override fun onUi() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mAdapter.set(mList)
            }
        })
    }

    override val searchQuery: CharSequence?
        get() = mSearchView?.query

    override val selectedPlayer: AbsPlayer?
        get() = mSelectedPlayer ?: mIdentityManager.identity

    private fun showEmpty() {
        mAdapter.clear()
        mRecyclerView.visibility = View.GONE
        mError.visibility = View.GONE
        mEmpty.visibility = View.VISIBLE
        mRefreshLayout.isRefreshing = false
        refreshMenu()
    }

    private fun showError() {
        mAdapter.clear()
        mRecyclerView.visibility = View.GONE
        mEmpty.visibility = View.GONE
        mError.visibility = View.VISIBLE
        mRefreshLayout.isRefreshing = false
        refreshMenu()
    }

    private fun showPlayersBundle() {
        mAdapter.set(mPlayersBundle)
        mEmpty.visibility = View.GONE
        mError.visibility = View.GONE
        mRecyclerView.visibility = View.VISIBLE
        mRefreshLayout.isRefreshing = false
        mRefreshLayout.isEnabled = false
        refreshMenu()
    }

    override val showUpNavigation = true

    override fun success(playersBundle: PlayersBundle?) {
        mSelectedPlayer = null
        mPlayersBundle = playersBundle

        if (playersBundle != null && playersBundle.hasPlayers()) {
            showPlayersBundle()
        } else {
            showEmpty()
        }
    }

}

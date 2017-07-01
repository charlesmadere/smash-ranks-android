package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.kotlin.orFalse
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.models.Region
import com.garpr.android.views.PlayersLayout
import com.garpr.android.views.toolbars.SearchToolbar
import kotterknife.bindView
import javax.inject.Inject

class PlayersActivity : BaseActivity(), MenuItemCompat.OnActionExpandListener,
        PlayersLayout.Listener, Searchable, SearchQueryHandle, SearchToolbar.Listener,
        SearchView.OnQueryTextListener {

    private var mSearchView: SearchView? = null

    @Inject
    lateinit protected var mRegionManager: RegionManager

    private val mPlayersLayout: PlayersLayout by bindView(R.id.playersLayout)


    companion object {
        private val TAG = "PlayersActivity"

        @JvmOverloads
        fun getLaunchIntent(context: Context, region: Region? = null): Intent {
            val intent = Intent(context, PlayersActivity::class.java)

            if (region != null) {
                intent.putExtra(BaseActivity.EXTRA_REGION, region)
            }

            return intent
        }
    }

    override val activityName = TAG

    override fun getSearchQuery(): CharSequence? = mSearchView?.query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_players)
        subtitle = mRegionManager.getRegion(this).displayName
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_players, menu)

        mPlayersLayout.mPlayersBundle?.let {
            val searchMenuItem = menu.findItem(R.id.miSearch)
            searchMenuItem.isVisible = true

            MenuItemCompat.setOnActionExpandListener(searchMenuItem, this)
            mSearchView = MenuItemCompat.getActionView(searchMenuItem) as SearchView

            mSearchView?.let {
                it.queryHint = getText(R.string.search_players_)
                it.setOnQueryTextListener(this)
            } ?: throw RuntimeException("mSearchView is null")
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        search(null)
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem) = true

    override fun onPlayersBundleFetched(layout: PlayersLayout) = supportInvalidateOptionsMenu()

    override fun onQueryTextChange(newText: String): Boolean {
        search(newText)
        return false
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        search(query)
        return false
    }

    override fun search(query: String?) = mPlayersLayout.search(query)

    override fun showSearchMenuItem() = mPlayersLayout.mPlayersBundle?.hasPlayers() ?: false

    override fun showUpNavigation() = true

}

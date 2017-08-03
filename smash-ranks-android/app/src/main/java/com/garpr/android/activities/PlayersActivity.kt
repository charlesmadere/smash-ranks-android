package com.garpr.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.extensions.subtitle
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.models.AbsRegion
import com.garpr.android.views.PlayersLayout
import com.garpr.android.views.toolbars.SearchToolbar
import kotterknife.bindView
import javax.inject.Inject

class PlayersActivity : BaseActivity(), MenuItem.OnActionExpandListener, PlayersLayout.Listener,
        Searchable, SearchQueryHandle, SearchToolbar.Listener, SearchView.OnQueryTextListener {

    private var mSearchView: SearchView? = null

    @Inject
    lateinit protected var mRegionManager: RegionManager

    private val mPlayersLayout: PlayersLayout by bindView(R.id.playersLayout)


    companion object {
        private const val TAG = "PlayersActivity"

        @JvmOverloads
        fun getLaunchIntent(context: Context, region: AbsRegion? = null): Intent {
            val intent = Intent(context, PlayersActivity::class.java)

            if (region != null) {
                intent.putExtra(BaseActivity.EXTRA_REGION, region)
            }

            return intent
        }
    }

    override val activityName = TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_players)
        subtitle = mRegionManager.getRegion(this).displayName
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_players, menu)

        val searchMenuItem = menu.findItem(R.id.miSearch) ?: throw RuntimeException(
                "searchMenuItem is null")
        searchMenuItem.isVisible = showSearchMenuItem
        searchMenuItem.setOnActionExpandListener(this)

        val searchView = searchMenuItem.actionView as SearchView
        searchView.queryHint = getText(R.string.search_)
        searchView.setOnQueryTextListener(this)
        mSearchView = searchView

        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        search(null)
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem) = true

    override fun onPlayersBundleFetched(layout: PlayersLayout) = invalidateOptionsMenu()

    override fun onQueryTextChange(newText: String): Boolean {
        search(newText)
        return false
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        search(query)
        return false
    }

    override fun search(query: String?) = mPlayersLayout.search(query)

    override val searchQuery: CharSequence?
        get() = mSearchView?.query

    override val showSearchMenuItem: Boolean
        get() = mPlayersLayout.mPlayersBundle?.players?.isNotEmpty() == true

    override val showUpNavigation = true

}

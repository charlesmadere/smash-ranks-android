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
import com.garpr.android.models.Region
import com.garpr.android.views.PlayersLayout
import com.garpr.android.views.toolbars.SearchToolbar
import kotterknife.bindView
import javax.inject.Inject

class PlayersActivity : BaseActivity(), MenuItem.OnActionExpandListener, PlayersLayout.Listener,
        Searchable, SearchQueryHandle, SearchToolbar.Listener, SearchView.OnQueryTextListener {

    private var searchView: SearchView? = null

    @Inject
    protected lateinit var regionManager: RegionManager

    private val playersLayout: PlayersLayout by bindView(R.id.playersLayout)


    companion object {
        private const val TAG = "PlayersActivity"

        fun getLaunchIntent(context: Context, region: Region? = null): Intent {
            val intent = Intent(context, PlayersActivity::class.java)

            if (region != null) {
                intent.putExtra(EXTRA_REGION, region)
            }

            return intent
        }
    }

    override val activityName = TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get().appComponent.inject(this)
        setContentView(R.layout.activity_players)
        subtitle = regionManager.getRegion(this).displayName
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
        this.searchView = searchView

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

    override fun search(query: String?) = playersLayout.search(query)

    override val searchQuery: CharSequence?
        get() = searchView?.query

    override val showSearchMenuItem: Boolean
        get() = playersLayout.playersBundle?.players?.isNotEmpty() == true

    override val showUpNavigation = true

}

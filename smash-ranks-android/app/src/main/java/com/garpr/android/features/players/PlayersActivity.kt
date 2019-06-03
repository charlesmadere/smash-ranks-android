package com.garpr.android.features.players

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.garpr.android.R
import com.garpr.android.activities.BaseActivity
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.putOptionalExtra
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.SearchQueryHandle
import com.garpr.android.misc.Searchable
import com.garpr.android.views.toolbars.SearchToolbar
import kotlinx.android.synthetic.main.activity_players.*
import kotlinx.android.synthetic.main.layout_players.*
import javax.inject.Inject

class PlayersActivity : BaseActivity(), PlayersLayout.Listener, Searchable, SearchQueryHandle,
        SearchToolbar.Listener {

    @Inject
    protected lateinit var regionManager: RegionManager


    companion object {
        private const val TAG = "PlayersActivity"

        fun getLaunchIntent(context: Context, region: Region? = null): Intent {
            return Intent(context, PlayersActivity::class.java)
                    .putOptionalExtra(EXTRA_REGION, region)
        }
    }

    override val activityName = TAG

    override fun onBackPressed() {
        if (toolbar.isSearchFieldExpanded) {
            toolbar.closeSearchField()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(R.layout.activity_players)
        toolbar.subtitleText = regionManager.getRegion(this).displayName
    }

    override fun onPlayersBundleFetched(layout: PlayersLayout) {
        toolbar.refresh()
    }

    override fun search(query: String?) {
        playersLayout.search(query)
    }

    override val searchQuery: CharSequence?
        get() = toolbar.searchQuery

    override val showSearchIcon: Boolean
        get() = playersLayout.playersBundle?.players?.isNotEmpty() == true

}

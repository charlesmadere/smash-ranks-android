package com.garpr.android.features.home

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.garpr.android.data.models.RankingCriteria
import com.garpr.android.data.models.RankingsBundle
import com.garpr.android.features.favoritePlayers.FavoritePlayersLayout
import com.garpr.android.features.rankings.RankingsLayout
import com.garpr.android.features.tournaments.TournamentsLayout
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.ListLayout
import com.garpr.android.misc.RankingCriteriaHandle
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Searchable
import java.lang.ref.WeakReference

class HomePagerAdapter : PagerAdapter(), RankingCriteriaHandle, Refreshable, Searchable {

    private val pages = mutableMapOf<HomeTab, WeakReference<View?>?>()


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        pages.remove(homeTab(position))
    }

    override fun getCount() = HomeTab.values().size

    private fun homeTab(position: Int): HomeTab {
        return HomeTab.values()[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val homeTab = homeTab(position)
        val view: View = when (homeTab) {
            HomeTab.RANKINGS -> RankingsLayout.inflate(container)
            HomeTab.TOURNAMENTS -> TournamentsLayout.inflate(container)
            HomeTab.FAVORITE_PLAYERS -> FavoritePlayersLayout.inflate(container)
        }

        container.addView(view)
        pages[homeTab] = WeakReference(view)

        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    fun onNavigationItemReselected(homeTab: HomeTab) {
        val view = pages[homeTab]?.get()

        if ((view as? Heartbeat?)?.isAlive == true) {
            (view as? ListLayout?)?.smoothScrollToTop()
        }
    }

    val rankingsBundle: RankingsBundle?
        get() {
            val view = pages[HomeTab.RANKINGS]?.get()
            return if ((view as? RankingsLayout)?.isAlive == true) view.rankingsBundle else null
        }

    override val rankingCriteria: RankingCriteria?
        get() = rankingsBundle?.rankingCriteria

    override fun refresh() {
        pages.values.forEach {
            val view = it?.get()

            if ((view as? Heartbeat?)?.isAlive == true) {
                (view as? Refreshable?)?.refresh()
            }
        }
    }

    override fun search(query: String?) {
        pages.values.forEach {
            val view = it?.get()

            if ((view as? Heartbeat?)?.isAlive == true) {
                (view as? Searchable?)?.search(query)
            }
        }
    }

}

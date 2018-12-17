package com.garpr.android.adapters

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.HomeTab
import com.garpr.android.misc.ListLayout
import com.garpr.android.misc.RankingCriteriaHandle
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Searchable
import com.garpr.android.models.RankingCriteria
import com.garpr.android.models.RankingsBundle
import com.garpr.android.views.FavoritePlayersLayout
import com.garpr.android.views.RankingsLayout
import com.garpr.android.views.TournamentsLayout
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

        if ((view as? Heartbeat)?.isAlive == true) {
            (view as? ListLayout)?.smoothScrollToTop()
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
        pages.forEach {
            if ((it.value as? Heartbeat)?.isAlive == true) {
                (it.value as? Refreshable)?.refresh()
            }
        }
    }

    override fun search(query: String?) {
        pages.forEach {
            if ((it.value as? Heartbeat)?.isAlive == true) {
                (it.value as? Searchable)?.search(query)
            }
        }
    }

}

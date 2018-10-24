package com.garpr.android.adapters

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.garpr.android.misc.*
import com.garpr.android.models.RankingCriteria
import com.garpr.android.models.RankingsBundle
import com.garpr.android.views.FavoritePlayersLayout
import com.garpr.android.views.RankingsLayout
import com.garpr.android.views.TournamentsLayout
import java.lang.ref.WeakReference

class HomePagerAdapter : PagerAdapter(), RankingCriteriaHandle, Refreshable {

    private val pages = mutableMapOf<HomeTab, WeakReference<View?>?>()


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        pages.remove(HomeTab.from(position))
    }

    override fun getCount() = HomeTab.values().size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val homeTab = HomeTab.from(position)
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

    fun search(homeTab: HomeTab, query: String?) {
        pages[homeTab]?.get()?.let {
            if ((it as? Heartbeat)?.isAlive == true) {
                (it as? Searchable)?.search(query)
            }
        }
    }

}

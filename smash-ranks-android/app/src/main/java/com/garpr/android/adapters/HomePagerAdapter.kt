package com.garpr.android.adapters

import android.support.v4.util.SparseArrayCompat
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.garpr.android.activities.HomeActivity
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.ListLayout
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Searchable
import com.garpr.android.models.RankingsBundle
import com.garpr.android.views.FavoritePlayersLayout
import com.garpr.android.views.RankingsLayout
import com.garpr.android.views.TournamentsLayout
import java.lang.ref.WeakReference

class HomePagerAdapter : PagerAdapter(), Refreshable {

    private val pages = SparseArrayCompat<WeakReference<View>>(count)


    companion object {
        private const val POSITION_FAVORITE_PLAYERS = HomeActivity.POSITION_FAVORITE_PLAYERS
        private const val POSITION_RANKINGS = HomeActivity.POSITION_RANKINGS
        private const val POSITION_TOURNAMENTS = HomeActivity.POSITION_TOURNAMENTS
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        pages.removeAt(position)
    }

    override fun getCount() = 3

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = when (position) {
            POSITION_FAVORITE_PLAYERS -> FavoritePlayersLayout.inflate(container)
            POSITION_RANKINGS -> RankingsLayout.inflate(container)
            POSITION_TOURNAMENTS -> TournamentsLayout.inflate(container)
            else -> throw RuntimeException("illegal position: $position")
        }

        container.addView(view)
        pages.put(position, WeakReference(view))

        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    fun onNavigationItemReselected(position: Int) {
        val view = pages[position].get()

        if ((view as? Heartbeat)?.isAlive == true) {
            (view as? ListLayout)?.smoothScrollToTop()
        }
    }

    val rankingsBundle: RankingsBundle?
        get() {
            val view = pages[POSITION_RANKINGS]?.get()
            return if ((view as? RankingsLayout)?.isAlive == true) view.rankingsBundle else null
        }

    override fun refresh() {
        for (i in 0 until pages.size()) {
            val view = pages[i].get()

            if ((view as? Heartbeat)?.isAlive == true) {
                (view as? Refreshable)?.refresh()
            }
        }
    }

    fun search(page: Int, query: String?) {
        val view: View? = when (page) {
            POSITION_RANKINGS -> {
                pages[POSITION_RANKINGS]?.get()
            }

            POSITION_TOURNAMENTS -> {
                pages[POSITION_TOURNAMENTS]?.get()
            }

            POSITION_FAVORITE_PLAYERS -> {
                pages[POSITION_FAVORITE_PLAYERS]?.get()
            }

            else -> {
                throw IllegalArgumentException("illegal page: $page")
            }
        }

        if ((view as? Heartbeat)?.isAlive == true) {
            (view as? Searchable)?.search(query)
        }
    }

}

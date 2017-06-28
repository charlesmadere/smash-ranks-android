package com.garpr.android.adapters

import android.support.v4.util.SparseArrayCompat
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

import com.garpr.android.activities.HomeActivity
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Searchable
import com.garpr.android.views.FavoritePlayersLayout
import com.garpr.android.views.RankingsLayout
import com.garpr.android.views.SearchableFrameLayout
import com.garpr.android.views.TournamentsLayout

import java.lang.ref.WeakReference

class HomePagerAdapter : PagerAdapter(), Refreshable, Searchable {

    private val mPages: SparseArrayCompat<WeakReference<SearchableFrameLayout>>


    companion object {
        private val POSITION_FAVORITE_PLAYERS = HomeActivity.PositionFavoritePlayers
        private val POSITION_RANKINGS = HomeActivity.PositionRankings
        private val POSITION_TOURNAMENTS = HomeActivity.PositionTournaments
    }

    init {
        mPages = SparseArrayCompat<WeakReference<SearchableFrameLayout>>(count)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        mPages.removeAt(position)
    }

    override fun getCount(): Int {
        return 3
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: SearchableFrameLayout

        when (position) {
            POSITION_FAVORITE_PLAYERS -> view = FavoritePlayersLayout.inflate(container)
            POSITION_TOURNAMENTS -> view = TournamentsLayout.inflate(container)
            POSITION_RANKINGS -> view = RankingsLayout.inflate(container)
            else -> throw RuntimeException("illegal position: " + position)
        }

        container.addView(view)
        mPages.put(position, WeakReference(view))

        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun refresh() {
        for (i in 0..mPages.size() - 1) {
            val reference = mPages.get(i)

            if (reference != null) {
                val view = reference.get()

                if (view is Refreshable && view.isAlive) {
                    view.refresh()
                }
            }
        }
    }

    override fun search(query: String?) {
        for (i in 0..mPages.size() - 1) {
            val reference = mPages.get(i)

            if (reference != null) {
                val view = reference.get()

                if (view != null && view.isAlive) {
                    view.search(query)
                }
            }
        }
    }

}

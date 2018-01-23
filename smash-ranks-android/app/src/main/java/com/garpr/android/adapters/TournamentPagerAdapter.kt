package com.garpr.android.adapters

import android.content.Context
import android.support.v4.util.SparseArrayCompat
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

import com.garpr.android.R
import com.garpr.android.misc.Searchable
import com.garpr.android.models.FullTournament
import com.garpr.android.views.TournamentMatchesLayout
import com.garpr.android.views.TournamentPageLayout
import com.garpr.android.views.TournamentPlayersLayout

import java.lang.ref.WeakReference

class TournamentPagerAdapter(
        private val context: Context,
        private val tournament: FullTournament
) : PagerAdapter(), Searchable {

    private val pages = SparseArrayCompat<WeakReference<TournamentPageLayout>>(count)


    companion object {
        private const val POSITION_MATCHES = 0
        private const val POSITION_PLAYERS = 1
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        pages.removeAt(position)
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            POSITION_MATCHES -> context.getString(R.string.matches)
            POSITION_PLAYERS -> context.getString(R.string.players)
            else -> throw RuntimeException("illegal position: " + position)
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = when (position) {
            POSITION_MATCHES -> TournamentMatchesLayout.inflate(container)
            POSITION_PLAYERS -> TournamentPlayersLayout.inflate(container)
            else -> throw RuntimeException("illegal position: " + position)
        }

        view.setContent(tournament)
        container.addView(view)
        pages.put(position, WeakReference(view))

        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    fun onTabReselected(position: Int) {
        val view = pages[position].get()

        if (view?.isAlive == true) {
            view.smoothScrollToTop()
        }
    }

    override fun search(query: String?) {
        for (i in 0 until pages.size()) {
            val reference = pages[i]

            if (reference != null) {
                val view = reference.get()

                if (view?.isAlive == true) {
                    view.search(query)
                }
            }
        }
    }

}

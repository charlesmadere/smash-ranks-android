package com.garpr.android.adapters

import android.content.Context
import android.support.v4.util.SparseArrayCompat
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

import com.garpr.android.R
import com.garpr.android.misc.ListLayout
import com.garpr.android.misc.Searchable
import com.garpr.android.models.FullTournament
import com.garpr.android.views.TournamentInfoLayout
import com.garpr.android.views.TournamentMatchesLayout
import com.garpr.android.views.TournamentPagerAdapterView
import com.garpr.android.views.TournamentPlayersLayout

import java.lang.ref.WeakReference

class TournamentPagerAdapter(
        private val context: Context,
        private val tournament: FullTournament
) : PagerAdapter(), Searchable {

    private val pages = SparseArrayCompat<WeakReference<TournamentPagerAdapterView>>(count)


    companion object {
        private const val POSITION_INFO = 0
        private const val POSITION_MATCHES = 1
        private const val POSITION_PLAYERS = 2
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        pages.removeAt(position)
    }

    override fun getCount() = 3

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            POSITION_INFO -> context.getText(R.string.info)
            POSITION_MATCHES -> context.getText(R.string.matches)
            POSITION_PLAYERS -> context.getText(R.string.players)
            else -> throw RuntimeException("illegal position: $position")
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: TournamentPagerAdapterView = when (position) {
            POSITION_INFO -> TournamentInfoLayout.inflate(container)
            POSITION_MATCHES -> TournamentMatchesLayout.inflate(container)
            POSITION_PLAYERS -> TournamentPlayersLayout.inflate(container)
            else -> throw RuntimeException("illegal position: $position")
        }

        if (view !is View) {
            throw RuntimeException("view: $view is not an Android View")
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
            (view as? ListLayout)?.smoothScrollToTop()
        }
    }

    override fun search(query: String?) {
        for (i in 0 until pages.size()) {
            val reference = pages[i]

            if (reference != null) {
                val view = reference.get()

                if (view?.isAlive == true) {
                    (view as? Searchable)?.search(query)
                }
            }
        }
    }

}

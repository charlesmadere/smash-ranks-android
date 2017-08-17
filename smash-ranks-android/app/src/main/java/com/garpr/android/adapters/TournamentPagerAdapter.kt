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
        private val mContext: Context,
        private val mTournament: FullTournament
) : PagerAdapter(), Searchable {

    private val mPages: SparseArrayCompat<WeakReference<TournamentPageLayout>> = SparseArrayCompat(count)


    companion object {
        const val POSITION_MATCHES = 0
        const val POSITION_PLAYERS = 1
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        mPages.removeAt(position)
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            POSITION_MATCHES -> return mContext.getString(R.string.matches)
            POSITION_PLAYERS -> return mContext.getString(R.string.players)
            else -> throw RuntimeException("illegal position: " + position)
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = when (position) {
            POSITION_MATCHES -> TournamentMatchesLayout.inflate(container)
            POSITION_PLAYERS -> TournamentPlayersLayout.inflate(container)
            else -> throw RuntimeException("illegal position: " + position)
        }

        view.setContent(mTournament)
        container.addView(view)
        mPages.put(position, WeakReference(view))

        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    fun onTabReselected(position: Int) {
        val view = mPages[position]?.get()

        if (view != null && view.isAlive) {
            view.scrollToTop()
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

package com.garpr.android.features.tournament

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.garpr.android.data.models.TournamentMode
import com.garpr.android.features.common.fragments.BaseFragment
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.ListLayout
import java.lang.ref.WeakReference

class TournamentFragmentPagerAdapter(
        fm: FragmentManager
) : FragmentPagerAdapter(fm) {

    private val pages = mutableMapOf<TournamentMode, WeakReference<BaseFragment?>?>()


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        pages.remove(TournamentMode.values()[position])
    }

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        val tournamentMode = TournamentMode.values()[position]

        val fragment: BaseFragment = when (tournamentMode) {
            TournamentMode.MATCHES -> TournamentMatchesFragment.create()
            TournamentMode.PLAYERS -> TournamentPlayersFragment.create()
        }

        pages[tournamentMode] = WeakReference(fragment)
        return fragment
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = super.instantiateItem(container, position)

        val tournamentMode = TournamentMode.values()[position]
        val fragment = item as? BaseFragment?

        if (fragment == null) {
            pages.remove(tournamentMode)
        } else {
            pages[tournamentMode] = WeakReference(fragment)
        }

        return item
    }

    fun onNavigationItemReselected(tournamentMode: TournamentMode) {
        val fragment = pages[tournamentMode]?.get()

        if ((fragment as? Heartbeat?)?.isAlive == true) {
            (fragment as? ListLayout?)?.smoothScrollToTop()
        }
    }

}

package com.garpr.android.features.home

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.garpr.android.features.common.fragments.BaseFragment
import com.garpr.android.features.favoritePlayers.FavoritePlayersFragment
import com.garpr.android.features.rankings.RankingsFragment
import com.garpr.android.features.tournaments.TournamentsFragment
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.ListLayout
import java.lang.ref.WeakReference

class HomeFragmentPagerAdapter(
        fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

    private val pages = mutableMapOf<HomeTab, WeakReference<BaseFragment?>?>()


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        pages.remove(HomeTab.values()[position])
    }

    override fun getCount(): Int = HomeTab.values().size

    override fun getItem(position: Int): Fragment {
        val homeTab = HomeTab.values()[position]

        val fragment: BaseFragment = when (homeTab) {
            HomeTab.RANKINGS -> RankingsFragment.create()
            HomeTab.TOURNAMENTS -> TournamentsFragment.create()
            HomeTab.FAVORITE_PLAYERS -> FavoritePlayersFragment.create()
        }

        pages[homeTab] = WeakReference(fragment)
        return fragment
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = super.instantiateItem(container, position)

        val homeTab = HomeTab.values()[position]
        val fragment = item as? BaseFragment?

        if (fragment == null) {
            pages.remove(homeTab)
        } else {
            pages[homeTab] = WeakReference(fragment)
        }

        return item
    }

    fun onNavigationItemReselected(homeTab: HomeTab) {
        val fragment = pages[homeTab]?.get()

        if ((fragment as? Heartbeat?)?.isAlive == true) {
            (fragment as? ListLayout?)?.smoothScrollToTop()
        }
    }

}

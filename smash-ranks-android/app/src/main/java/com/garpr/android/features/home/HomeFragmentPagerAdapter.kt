package com.garpr.android.features.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.garpr.android.features.common.fragments.BaseFragment
import com.garpr.android.features.rankings.RankingsAndFavoritesFragment
import com.garpr.android.features.tournaments.TournamentsFragment
import com.garpr.android.misc.ListLayout
import java.lang.ref.WeakReference

class HomeFragmentPagerAdapter(
        activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    private val pages = mutableMapOf<HomeTab, WeakReference<BaseFragment?>?>()

    override fun createFragment(position: Int): Fragment {
        val homeTab = HomeTab.values()[position]

        val fragment: BaseFragment = when (homeTab) {
            HomeTab.HOME -> RankingsAndFavoritesFragment.create()
            HomeTab.TOURNAMENTS -> TournamentsFragment.create()
        }

        pages[homeTab] = WeakReference(fragment)
        return fragment
    }

    override fun getItemCount(): Int = HomeTab.values().size

    fun onHomeTabReselected(homeTab: HomeTab) {
        val fragment = pages[homeTab]?.get()

        if (fragment?.isAlive == true) {
            (fragment as? ListLayout?)?.smoothScrollToTop()
        }
    }

}

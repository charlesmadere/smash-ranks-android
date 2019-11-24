package com.garpr.android.features.tournament

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.garpr.android.data.models.TournamentMode
import com.garpr.android.features.common.fragments.BaseFragment
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.ListLayout
import java.lang.ref.WeakReference

class TournamentFragmentPagerAdapter(
        activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    private val pages = mutableMapOf<TournamentMode, WeakReference<BaseFragment?>?>()

    override fun createFragment(position: Int): Fragment {
        val tournamentMode = TournamentMode.values()[position]

        val fragment: BaseFragment = when (tournamentMode) {
            TournamentMode.MATCHES -> TournamentMatchesFragment.create()
            TournamentMode.PLAYERS -> TournamentPlayersFragment.create()
        }

        pages[tournamentMode] = WeakReference(fragment)
        return fragment
    }

    override fun getItemCount(): Int = TournamentMode.values().size

    fun onNavigationItemReselected(tournamentMode: TournamentMode) {
        val fragment = pages[tournamentMode]?.get()

        if ((fragment as? Heartbeat?)?.isAlive == true) {
            (fragment as? ListLayout?)?.smoothScrollToTop()
        }
    }

}

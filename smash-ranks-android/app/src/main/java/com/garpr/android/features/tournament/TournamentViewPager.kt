package com.garpr.android.features.tournament

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager
import com.garpr.android.data.models.TournamentMode

class TournamentViewPager @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    var currentTab: TournamentMode
        get() = TournamentMode.values()[currentItem]
        set(value) { currentItem = value.ordinal }

}

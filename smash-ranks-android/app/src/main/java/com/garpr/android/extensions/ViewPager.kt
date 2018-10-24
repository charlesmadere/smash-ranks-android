package com.garpr.android.extensions

import androidx.viewpager.widget.ViewPager
import com.garpr.android.misc.HomeTab

var ViewPager.currentItemAsHomeTab: HomeTab
    get() = HomeTab.values()[currentItem]
    set(value) { setCurrentItem(value.ordinal, false) }

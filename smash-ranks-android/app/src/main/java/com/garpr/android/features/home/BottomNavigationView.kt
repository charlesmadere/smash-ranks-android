package com.garpr.android.features.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View.OnClickListener
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.garpr.android.R
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.setTintedImageColor
import com.garpr.android.misc.Refreshable
import kotlinx.android.synthetic.main.view_bottom_navigation.view.*

class BottomNavigationView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), Refreshable {

    private val selectionDrawable: Drawable? = ContextCompat.getDrawable(context,
            R.drawable.bottom_navigation_selection)

    var selection: HomeTab = HomeTab.HOME
        set(value) {
            field = value
            refresh()
        }

    @ColorInt
    private val colorAccent: Int = context.getAttrColor(R.attr.colorAccent)

    @ColorInt
    private val textColorPrimary: Int = context.getAttrColor(android.R.attr.textColorPrimary)

    var listeners: Listeners? = null

    private val homeTabClickListener = OnClickListener {
        if (selection == HomeTab.HOME) {
            listeners?.onHomeTabReselected(this, HomeTab.HOME)
        } else {
            selection = HomeTab.HOME
            listeners?.onHomeTabClick(this, HomeTab.HOME)
        }
    }

    private val tournamentsTabClickListener = OnClickListener {
        if (selection == HomeTab.TOURNAMENTS) {
            listeners?.onHomeTabReselected(this, HomeTab.TOURNAMENTS)
        } else {
            selection = HomeTab.TOURNAMENTS
            listeners?.onHomeTabClick(this, HomeTab.TOURNAMENTS)
        }
    }

    init {
        layoutInflater.inflate(R.layout.view_bottom_navigation, this)
        homeClickArea.setOnClickListener(homeTabClickListener)
        tournamentsClickArea.setOnClickListener(tournamentsTabClickListener)
        refresh()
    }

    override fun refresh() {
        when (selection) {
            HomeTab.HOME -> {
                ViewCompat.setBackground(homeCell, selectionDrawable)
                homeImageView.setTintedImageColor(colorAccent)
                homeTextView.setTextColor(colorAccent)

                ViewCompat.setBackground(tournamentsCell, null)
                tournamentsImageView.setTintedImageColor(textColorPrimary)
                tournamentsTextView.setTextColor(textColorPrimary)
            }

            HomeTab.TOURNAMENTS -> {
                ViewCompat.setBackground(homeCell, null)
                homeImageView.setTintedImageColor(textColorPrimary)
                homeTextView.setTextColor(textColorPrimary)

                ViewCompat.setBackground(tournamentsCell, selectionDrawable)
                tournamentsImageView.setTintedImageColor(colorAccent)
                tournamentsTextView.setTextColor(colorAccent)
            }
        }
    }

    interface Listeners {
        fun onHomeTabClick(v: BottomNavigationView, homeTab: HomeTab)
        fun onHomeTabReselected(v: BottomNavigationView, homeTab: HomeTab)
    }

}

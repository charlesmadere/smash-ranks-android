package com.garpr.android.views.toolbars

import android.content.Context
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import com.garpr.android.R

class TournamentToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : SearchToolbar(context, attrs) {

    override fun onCreateOptionsMenu(inflater: MenuInflater, menu: Menu) {
        inflater.inflate(R.menu.toolbar_tournament, menu)
        super.onCreateOptionsMenu(inflater, menu)
    }

}

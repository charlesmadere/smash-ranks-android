package com.garpr.android.views.toolbars

import android.content.Context
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import com.garpr.android.R
import com.garpr.android.extensions.optActivity
import com.garpr.android.misc.TournamentToolbarManager
import com.garpr.android.models.FullTournament
import javax.inject.Inject

class TournamentToolbar : SearchToolbar {

    @Inject
    lateinit protected var tournamentToolbarManager: TournamentToolbarManager


    interface DataProvider {
        val fullTournament: FullTournament?
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun onCreateOptionsMenu(inflater: MenuInflater, menu: Menu) {
        inflater.inflate(R.menu.toolbar_tournament, menu)
        super.onCreateOptionsMenu(inflater, menu)
    }

    override fun onRefreshMenu() {
        super.onRefreshMenu()

        if (isSearchLayoutExpanded) {
            return
        }

        val activity = context.optActivity()
        val fullTournament: FullTournament? = if (activity is DataProvider)
            activity.fullTournament else null

        val presentation = tournamentToolbarManager.getPresentation(fullTournament)
        menu.findItem(R.id.miShare).isVisible = presentation.mIsShareVisible
        menu.findItem(R.id.miViewTournamentPage).isVisible = presentation.mIsViewTournamentPageVisible
    }

}

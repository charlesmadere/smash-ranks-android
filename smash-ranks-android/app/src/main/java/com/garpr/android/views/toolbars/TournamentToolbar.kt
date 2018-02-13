package com.garpr.android.views.toolbars

import android.content.Context
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.extensions.optActivity
import com.garpr.android.misc.TournamentToolbarManager
import com.garpr.android.models.FullTournament
import javax.inject.Inject

class TournamentToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
) : SearchToolbar(context, attrs, defStyleAttr) {

    @Inject
    protected lateinit var tournamentToolbarManager: TournamentToolbarManager


    interface DataProvider {
        val fullTournament: FullTournament?
    }

    override fun onCreateOptionsMenu(inflater: MenuInflater, menu: Menu) {
        inflater.inflate(R.menu.toolbar_tournament, menu)
        super.onCreateOptionsMenu(inflater, menu)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
        }
    }

    override fun onRefreshMenu() {
        super.onRefreshMenu()

        if (isSearchLayoutExpanded) {
            return
        }

        val fullTournament = (context.optActivity() as? DataProvider)?.fullTournament
        val presentation = tournamentToolbarManager.getPresentation(fullTournament)
        menu.findItem(R.id.miShare).isVisible = presentation.isShareVisible
        menu.findItem(R.id.miViewTournamentPage).isVisible = presentation.isViewTournamentPageVisible
    }

}

package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.activities.TournamentActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.clear
import com.garpr.android.managers.RegionManager
import kotlinx.android.synthetic.main.divider_tournament.view.*
import javax.inject.Inject

class TournamentDividerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), BaseAdapterView<AbsTournament>, View.OnClickListener {

    @Inject
    protected lateinit var regionManager: RegionManager


    init {
        setOnClickListener(this)

        if (!isInEditMode) {
            appComponent.inject(this)
        }
    }

    var tournament: AbsTournament? = null
        private set(value) {
            field = value

            if (value == null) {
                clear()
                return
            }

            name.text = value.name
            date.text = value.date.mediumForm
        }

    private fun clear() {
        date.clear()
        name.clear()
    }

    override fun onClick(v: View) {
        val tournament = this.tournament ?: return
        context.startActivity(TournamentActivity.getLaunchIntent(context, tournament,
                regionManager.getRegion(context)))
    }

    override fun setContent(content: AbsTournament) {
        tournament = content
    }

}

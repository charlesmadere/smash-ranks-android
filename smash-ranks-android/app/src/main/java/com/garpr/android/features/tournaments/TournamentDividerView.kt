package com.garpr.android.features.tournaments

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.data.models.AbsTournament
import kotlinx.android.synthetic.main.divider_tournament.view.*

class TournamentDividerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), View.OnClickListener {

    private var _tournament: AbsTournament? = null

    val tournament: AbsTournament
        get() = checkNotNull(_tournament)

    var listener: Listener? = null

    interface Listener {
        fun onClick(v: TournamentDividerView)
    }

    init {
        setOnClickListener(this)
    }

    override fun onClick(v: View) {
        listener?.onClick(this)
    }

    fun setContent(tournament: AbsTournament) {
        _tournament = tournament
        name.text = tournament.name
        date.text = tournament.date.mediumForm
    }

}

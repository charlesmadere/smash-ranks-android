package com.garpr.android.features.tournament

import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.widget.LinearLayout
import com.garpr.android.R
import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.BracketSource
import com.garpr.android.data.models.FullTournament
import com.garpr.android.extensions.verticalPositionInWindow
import kotlinx.android.synthetic.main.item_tournament_info.view.*
import java.text.NumberFormat

class TournamentInfoView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var _tournament: FullTournament? = null

    val tournament: FullTournament
        get() = checkNotNull(_tournament)

    val dateVerticalPositionInWindow: Int
        get() = date.verticalPositionInWindow

    var listeners: Listeners? = null

    private val openLinkClickListener = OnClickListener {
        listeners?.onOpenLinkClick(this)
    }

    private val shareClickListener = OnClickListener {
        listeners?.onShareClick(this)
    }

    interface Listeners {
        fun onOpenLinkClick(v: TournamentInfoView)
        fun onShareClick(v: TournamentInfoView)
    }

    companion object {
        private val NUMBER_FORMAT = NumberFormat.getIntegerInstance()
    }

    init {
        orientation = VERTICAL

        inflate(context, R.layout.item_tournament_info, this)

        openLink.setOnClickListener(openLinkClickListener)
        share.setOnClickListener(shareClickListener)
    }

    fun setContent(tournament: FullTournament, region: AbsRegion) {
        _tournament = tournament

        name.text = tournament.name
        date.text = tournament.date.fullForm
        this.region.text = region.displayName

        val entrants = tournament.players?.size ?: 0
        entrantsCount.text = resources.getQuantityString(R.plurals.x_entrants, entrants,
                NUMBER_FORMAT.format(entrants))

        if (tournament.url.isNullOrBlank()) {
            actionButtons.visibility = GONE
        } else {
            openLink.setText(when (BracketSource.fromUrl(tournament.url)) {
                BracketSource.CHALLONGE -> R.string.open_challonge_link
                BracketSource.SMASH_GG -> R.string.open_smash_gg_link
                else -> R.string.open_bracket_link
            })

            actionButtons.visibility = VISIBLE
        }
    }

}

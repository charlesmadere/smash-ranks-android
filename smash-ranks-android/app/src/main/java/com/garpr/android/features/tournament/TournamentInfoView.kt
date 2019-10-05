package com.garpr.android.features.tournament

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import com.garpr.android.R
import com.garpr.android.data.models.BracketSource
import com.garpr.android.data.models.FullTournament
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.verticalPositionInWindow
import com.garpr.android.features.common.adapters.BaseAdapterView
import com.garpr.android.misc.Refreshable
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.item_tournament_info.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.text.NumberFormat

class TournamentInfoView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs), BaseAdapterView<FullTournament?>, KoinComponent, Refreshable {

    private var _tournament: FullTournament? = null
        set(value) {
            field = value
            refresh()
        }

    val tournament: FullTournament
        get() = requireNotNull(_tournament)

    val dateVerticalPositionInWindow: Int
        get() = date.verticalPositionInWindow

    var listeners: Listeners? = null

    private val openLinkClickListener = OnClickListener {
        listeners?.onOpenLinkClick(this)
    }

    private val shareClickListener = OnClickListener {
        listeners?.onShareClick(this)
    }

    protected val regionRepository: RegionRepository by inject()

    companion object {
        private val NUMBER_FORMAT = NumberFormat.getIntegerInstance()
    }

    init {
        orientation = VERTICAL

        inflate(context, R.layout.item_tournament_info, this)

        openLink.setOnClickListener(openLinkClickListener)
        share.setOnClickListener(shareClickListener)
    }

    override fun refresh() {
        val tournament = _tournament

        if (tournament == null) {
            name.clear()
            date.clear()
            region.clear()
            entrantsCount.clear()
            actionButtons.visibility = View.GONE
            return
        }

        name.text = tournament.name
        date.text = tournament.date.fullForm
        region.text = regionRepository.getRegion(context).displayName

        val entrants = tournament.players?.size ?: 0
        entrantsCount.text = resources.getQuantityString(R.plurals.x_entrants, entrants,
                NUMBER_FORMAT.format(entrants))

        if (tournament.url.isNullOrBlank()) {
            actionButtons.visibility = View.GONE
        } else {
            openLink.setText(when (BracketSource.fromUrl(tournament.url)) {
                BracketSource.CHALLONGE -> R.string.open_challonge_link
                BracketSource.SMASH_GG -> R.string.open_smash_gg_link
                else -> R.string.open_bracket_link
            })

            actionButtons.visibility = View.VISIBLE
        }
    }

    override fun setContent(content: FullTournament?) {
        _tournament = content
    }

    interface Listeners {
        fun onOpenLinkClick(v: TournamentInfoView)
        fun onShareClick(v: TournamentInfoView)
    }

}

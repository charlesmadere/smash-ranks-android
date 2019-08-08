package com.garpr.android.features.tournament

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.garpr.android.R
import com.garpr.android.data.models.BracketSource
import com.garpr.android.data.models.FullTournament
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.requireActivity
import com.garpr.android.extensions.verticalPositionInWindow
import com.garpr.android.misc.ShareUtils
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.item_tournament_info.view.*
import java.text.NumberFormat
import javax.inject.Inject

class TournamentInfoView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val numberFormat = NumberFormat.getIntegerInstance()

    @Inject
    protected lateinit var regionRepository: RegionRepository

    @Inject
    protected lateinit var shareUtils: ShareUtils

    var tournament: FullTournament? = null
        set(value) {
            field = value

            if (value == null) {
                name.clear()
                date.clear()
                region.clear()
                entrantsCount.clear()
                actionButtons.visibility = View.GONE
                return
            }

            name.text = value.name
            date.text = value.date.fullForm
            region.text = regionRepository.getRegion(context).displayName

            val entrants = value.players?.size ?: 0
            entrantsCount.text = resources.getQuantityString(R.plurals.x_entrants, entrants,
                    numberFormat.format(entrants))

            if (value.url.isNullOrBlank()) {
                actionButtons.visibility = View.GONE
            } else {
                openLink.setText(when (BracketSource.fromUrl(value.url)) {
                    BracketSource.CHALLONGE -> R.string.open_challonge_link
                    BracketSource.SMASH_GG -> R.string.open_smash_gg_link
                    else -> R.string.open_bracket_link
                })

                actionButtons.visibility = View.VISIBLE
            }
        }

    val dateVerticalPositionInWindow: Int
        get() = date.verticalPositionInWindow

    init {
        orientation = VERTICAL

        @Suppress("LeakingThis")
        layoutInflater.inflate(R.layout.item_tournament_info, this)

        if (!isInEditMode) {
            appComponent.inject(this)
        }

        openLink.setOnClickListener {
            tournament?.let { t -> shareUtils.openUrl(context, t.url) }
        }

        share.setOnClickListener {
            tournament?.let { t -> shareUtils.shareTournament(requireActivity(), t) }
        }
    }

}
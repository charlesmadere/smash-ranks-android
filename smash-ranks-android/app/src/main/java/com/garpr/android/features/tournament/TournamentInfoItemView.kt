package com.garpr.android.features.tournament

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.garpr.android.R
import com.garpr.android.data.models.BracketSource
import com.garpr.android.data.models.FullTournament
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.requireActivity
import com.garpr.android.extensions.verticalPositionInWindow
import com.garpr.android.features.base.BaseAdapterView
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.ShareUtils
import kotlinx.android.synthetic.main.item_tournament_info.view.*
import java.text.NumberFormat
import javax.inject.Inject

class TournamentInfoItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs), BaseAdapterView<FullTournament> {

    private val numberFormat = NumberFormat.getIntegerInstance()

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var shareUtils: ShareUtils


    companion object {
        fun inflate(parent: ViewGroup): TournamentInfoItemView = parent.layoutInflater.inflate(
                R.layout.item_tournament_info, parent, false) as TournamentInfoItemView
    }

    init {
        if (!isInEditMode) {
            appComponent.inject(this)
        }
    }

    val dateVerticalPositionInWindow: Int
        get() = date.verticalPositionInWindow

    override fun onFinishInflate() {
        super.onFinishInflate()

        openLink.setOnClickListener {
            tournament?.let { t -> shareUtils.openUrl(context, t.url) }
        }

        share.setOnClickListener {
            tournament?.let { t -> shareUtils.shareTournament(requireActivity(), t) }
        }
    }

    override fun setContent(content: FullTournament) {
        tournament = content
    }

    val tabsVerticalPositionInWindow: Int
        get() = tournamentTabsView.verticalPositionInWindow

    private var tournament: FullTournament? = null
        set(value) {
            field = value

            if (value == null) {
                return
            }

            name.text = value.name
            date.text = value.date.fullForm
            region.text = regionManager.getRegion(context).displayName

            val entrants = value.players?.size ?: 0
            entrantsCount.text = resources.getQuantityString(R.plurals.x_entrants, entrants,
                    numberFormat.format(entrants))

            if (value.url.isNullOrBlank()) {
                openLink.visibility = View.GONE
                share.visibility = View.GONE
            } else {
                openLink.setText(when (BracketSource.fromUrl(value.url)) {
                    BracketSource.CHALLONGE -> R.string.open_challonge_link
                    BracketSource.SMASH_GG -> R.string.open_smash_gg_link
                    else -> R.string.open_bracket_link
                })

                openLink.visibility = View.VISIBLE
                share.visibility = View.VISIBLE
            }
        }

}

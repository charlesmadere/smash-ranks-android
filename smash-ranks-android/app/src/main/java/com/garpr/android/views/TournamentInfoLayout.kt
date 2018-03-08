package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.extensions.getActivity
import com.garpr.android.misc.ShareUtils
import com.garpr.android.models.BracketSource
import com.garpr.android.models.FullTournament
import kotterknife.bindView
import java.text.NumberFormat
import javax.inject.Inject

class TournamentInfoLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleConstraintLayout(context, attrs), TournamentPagerAdapterView {

    private val numberFormat = NumberFormat.getIntegerInstance()

    @Inject
    protected lateinit var shareUtils: ShareUtils

    private val date: TextView by bindView(R.id.tvDate)
    private val entrants: TextView by bindView(R.id.tvEntrants)
    private val name: TextView by bindView(R.id.tvName)
    private val openLink: TextView by bindView(R.id.tvOpenLink)
    private val share: TextView by bindView(R.id.tvShare)


    companion object {
        fun inflate(parent: ViewGroup): TournamentInfoLayout = LayoutInflater.from(
                parent.context).inflate(R.layout.layout_tournament_info, parent,
                false) as TournamentInfoLayout
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
        }

        entrants.setOnClickListener {
            // TODO
        }

        openLink.setOnClickListener {
            tournament?.let { shareUtils.openUrl(context, it.url) }
        }

        share.setOnClickListener {
            tournament?.let { shareUtils.shareTournament(context.getActivity(), it) }
        }
    }

    private var tournament: FullTournament? = null
        set(value) {
            field = value

            if (value == null) {
                return
            }

            name.text = value.name
            date.text = value.date.fullForm

            val players = value.players?.size ?: 0
            entrants.text = resources.getQuantityString(R.plurals.x_entrants, players,
                    numberFormat.format(players))

            if (value.url.isNullOrBlank()) {
                openLink.visibility = View.GONE
                share.visibility = View.GONE
            } else {
                when (BracketSource.fromUrl(value.url)) {
                    BracketSource.CHALLONGE -> {
                        openLink.text = resources.getText(R.string.open_challonge_link)
                    }

                    BracketSource.SMASH_GG -> {
                        openLink.text = resources.getText(R.string.open_smash_gg_link)
                    }

                    else -> {
                        openLink.text = resources.getText(R.string.open_bracket_link)
                    }
                }

                openLink.visibility = View.VISIBLE
                share.visibility = View.VISIBLE
            }
        }

    override fun setContent(content: FullTournament) {
        tournament = content
    }

}

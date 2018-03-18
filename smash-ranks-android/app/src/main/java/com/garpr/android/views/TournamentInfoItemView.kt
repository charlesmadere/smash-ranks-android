package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.getActivity
import com.garpr.android.extensions.verticalPositionInWindow
import com.garpr.android.misc.RegionManager
import com.garpr.android.misc.ShareUtils
import com.garpr.android.models.BracketSource
import com.garpr.android.models.FullTournament
import kotterknife.bindView
import java.text.NumberFormat
import javax.inject.Inject

class TournamentInfoItemView : LinearLayout, BaseAdapterView<FullTournament> {

    private val numberFormat = NumberFormat.getIntegerInstance()

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var shareUtils: ShareUtils

    private val openLink: Button by bindView(R.id.bOpenLink)
    private val share: Button by bindView(R.id.bShare)
    private val date: TextView by bindView(R.id.tvDate)
    private val entrantsCount: TextView by bindView(R.id.tvEntrantsCount)
    private val name: TextView by bindView(R.id.tvName)
    private val region: TextView by bindView(R.id.tvRegion)
    private val tournamentTabsView: TournamentTabsView by bindView(R.id.tournamentTabsView)


    companion object {
        fun inflate(parent: ViewGroup): TournamentInfoItemView = LayoutInflater.from(
                parent.context).inflate(R.layout.item_tournament_info, parent,
                false) as TournamentInfoItemView
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
                @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    val dateVerticalPositionInWindow: Int
        get() = date.verticalPositionInWindow

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
        }

        openLink.setOnClickListener {
            tournament?.let { shareUtils.openUrl(context, it.url) }
        }

        share.setOnClickListener {
            tournament?.let { shareUtils.shareTournament(context.getActivity(), it) }
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

}

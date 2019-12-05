package com.garpr.android.features.tournament

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.FullTournament
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.features.common.adapters.BaseAdapterView
import com.garpr.android.features.common.views.IdentityConstraintLayout
import kotlinx.android.synthetic.main.item_tournament_match.view.*

class TournamentMatchItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : IdentityConstraintLayout(context, attrs), BaseAdapterView<FullTournament.Match>,
        View.OnClickListener {

    private var _match: FullTournament.Match? = null

    val match: FullTournament.Match
        get() = checkNotNull(_match)

    @ColorInt
    private val exclusionColor: Int = context.getAttrColor(android.R.attr.textColorSecondary)

    @ColorInt
    private val loseColor: Int = ContextCompat.getColor(context, R.color.lose)

    @ColorInt
    private val winColor: Int = ContextCompat.getColor(context, R.color.win)

    var listener: Listener? = null

    init {
        setOnClickListener(this)
    }

    override fun clear() {
        super.clear()
        loserName.clear()
        winnerName.clear()
    }

    override fun onClick(v: View) {
        listener?.onClick(this)
    }

    override fun refresh() {
        val match = _match

        if (match == null) {
            clear()
            return
        }

        loserName.text = match.loserName
        winnerName.text = match.winnerName

        if (match.isExcluded) {
            loserName.setTextColor(exclusionColor)
            winnerName.setTextColor(exclusionColor)
        } else {
            loserName.setTextColor(loseColor)
            winnerName.setTextColor(winColor)
        }

        if (identityRepository.isPlayer(match.winnerId)) {
            styleTextViewForUser(winnerName)
            styleTextViewForSomeoneElse(loserName)
            identityIsUser()
        } else if (identityRepository.isPlayer(match.loserId)) {
            styleTextViewForSomeoneElse(winnerName)
            styleTextViewForUser(loserName)
            identityIsUser()
        } else {
            styleTextViewForSomeoneElse(winnerName)
            styleTextViewForSomeoneElse(loserName)
            identityIsSomeoneElse()
        }
    }

    override fun setContent(content: FullTournament.Match) {
        _match = content
        refresh()
    }

    interface Listener {
        fun onClick(v: TournamentMatchItemView)
    }

}

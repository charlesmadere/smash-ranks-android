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
import com.garpr.android.extensions.requireFragmentActivity
import com.garpr.android.features.common.adapters.BaseAdapterView
import com.garpr.android.features.common.views.IdentityConstraintLayout
import kotlinx.android.synthetic.main.item_tournament_match.view.*

class TournamentMatchItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : IdentityConstraintLayout(context, attrs), BaseAdapterView<FullTournament.Match>,
        View.OnClickListener {

    @ColorInt
    private val exclusionColor: Int = context.getAttrColor(android.R.attr.textColorSecondary)

    @ColorInt
    private val loseColor: Int = ContextCompat.getColor(context, R.color.lose)

    @ColorInt
    private val winColor: Int = ContextCompat.getColor(context, R.color.win)

    init {
        setOnClickListener(this)
    }

    override fun clear() {
        loserName.clear()
        winnerName.clear()
        super.clear()
    }

    private var match: FullTournament.Match? = null
        set(value) {
            field = value

            if (value == null) {
                clear()
                return
            }

            loserName.text = value.loserName
            winnerName.text = value.winnerName

            if (value.isExcluded) {
                loserName.setTextColor(exclusionColor)
                winnerName.setTextColor(exclusionColor)
            } else {
                loserName.setTextColor(loseColor)
                winnerName.setTextColor(winColor)
            }

            refresh()
        }

    override fun onClick(v: View) {
        val match = this.match ?: return

        val dialog = TournamentMatchDialogFragment.create(match)
        dialog.show(requireFragmentActivity().supportFragmentManager,
                TournamentMatchDialogFragment.TAG)
    }

    override fun refresh() {
        val match = this.match

        if (match != null && identityRepository.isPlayer(match.winnerId)) {
            styleTextViewForUser(winnerName)
            styleTextViewForSomeoneElse(loserName)
            identityIsUser()
        } else if (match != null && identityRepository.isPlayer(match.loserId)) {
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
        match = content
    }

}

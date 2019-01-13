package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.data.models.FullTournament
import com.garpr.android.dialogs.TournamentMatchDialogFragment
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.requireFragmentActivity
import com.garpr.android.managers.RegionManager
import kotlinx.android.synthetic.main.item_tournament_match.view.*
import javax.inject.Inject

class TournamentMatchItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : IdentityConstraintLayout(context, attrs), BaseAdapterView<FullTournament.Match>,
        View.OnClickListener {

    @ColorInt
    private var exclusionColor: Int = 0

    @ColorInt
    private var loseColor: Int = 0

    @ColorInt
    private var winColor: Int = 0

    @Inject
    protected lateinit var regionManager: RegionManager


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

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
        }

        exclusionColor = context.getAttrColor(android.R.attr.textColorSecondary)
        loseColor = ContextCompat.getColor(context, R.color.lose)
        winColor = ContextCompat.getColor(context, R.color.win)

        setOnClickListener(this)
    }

    override fun refresh() {
        val match = this.match

        if (match != null && identityManager.isPlayer(match.winnerId)) {
            styleTextViewForUser(winnerName)
            styleTextViewForSomeoneElse(loserName)
            identityIsUser()
        } else if (match != null && identityManager.isPlayer(match.loserId)) {
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

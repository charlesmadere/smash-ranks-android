package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.data.models.HeadToHeadMatch
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.MatchResult
import com.garpr.android.dialogs.HeadToHeadDialogFragment
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.requireFragmentActivity
import com.garpr.android.managers.RegionManager
import kotlinx.android.synthetic.main.item_head_to_head_match.view.*
import javax.inject.Inject

class HeadToHeadMatchItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : IdentityConstraintLayout(context, attrs), BaseAdapterView<HeadToHeadMatch>,
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
        playerName.clear()
        opponentName.clear()
        super.clear()
    }

    private var match: HeadToHeadMatch? = null
        set(value) {
            field = value

            if (value == null) {
                clear()
                return
            }

            playerName.text = value.player.name
            opponentName.text = value.opponent.name

            when (value.result) {
                MatchResult.EXCLUDED -> {
                    playerName.setTextColor(exclusionColor)
                    opponentName.setTextColor(exclusionColor)
                }

                MatchResult.LOSE -> {
                    playerName.setTextColor(loseColor)
                    opponentName.setTextColor(winColor)
                }

                MatchResult.WIN -> {
                    playerName.setTextColor(winColor)
                    opponentName.setTextColor(loseColor)
                }
            }
        }

    override fun onClick(v: View) {
        val match = this.match ?: return
        val dialog = HeadToHeadDialogFragment.create(match)
        dialog.show(requireFragmentActivity().supportFragmentManager, HeadToHeadDialogFragment.TAG)
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

        if (isInEditMode) {
            setContent(HeadToHeadMatch(MatchResult.WIN, LitePlayer("0", "Shroomed"),
                    LitePlayer("1", "PewPewU")))
        }
    }

    override fun setContent(content: HeadToHeadMatch) {
        match = content
    }

}

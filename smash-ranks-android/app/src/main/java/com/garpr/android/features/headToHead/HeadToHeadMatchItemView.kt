package com.garpr.android.features.headToHead

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.HeadToHeadMatch
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.MatchResult
import com.garpr.android.extensions.getAttrColor
import kotlinx.android.synthetic.main.item_head_to_head_match.view.*

class HeadToHeadMatchItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    @ColorInt
    private val exclusionColor: Int = context.getAttrColor(android.R.attr.textColorSecondary)

    @ColorInt
    private val loseColor: Int = ContextCompat.getColor(context, R.color.lose)

    @ColorInt
    private val winColor: Int = ContextCompat.getColor(context, R.color.win)

    init {
        if (isInEditMode) {
            setContent(
                    match = HeadToHeadMatch(
                            result = MatchResult.WIN,
                            player = LitePlayer("0", "Shroomed"),
                            opponent = LitePlayer("1", "PewPewU")
                    ),
                    playerIsIdentity = true,
                    opponentIsIdentity = false
            )
        }
    }

    fun setContent(match: HeadToHeadMatch, playerIsIdentity: Boolean, opponentIsIdentity: Boolean) {
        playerName.text = match.player.name
        opponentName.text = match.opponent.name

        playerName.typeface = if (playerIsIdentity) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        opponentName.typeface = if (opponentIsIdentity) Typeface.DEFAULT_BOLD else Typeface.DEFAULT

        when (match.result) {
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

}

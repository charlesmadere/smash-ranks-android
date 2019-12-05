package com.garpr.android.features.headToHead

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.HeadToHeadMatch
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.MatchResult
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.features.common.adapters.BaseAdapterView
import com.garpr.android.features.common.views.IdentityConstraintLayout
import kotlinx.android.synthetic.main.item_head_to_head_match.view.*

class HeadToHeadMatchItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : IdentityConstraintLayout(context, attrs), BaseAdapterView<HeadToHeadMatch>,
        View.OnClickListener {

    private var _headToHeadMatch: HeadToHeadMatch? = null

    val headToHeadMatch: HeadToHeadMatch
        get() = checkNotNull(_headToHeadMatch)

    @ColorInt
    private val exclusionColor: Int = context.getAttrColor(android.R.attr.textColorSecondary)

    @ColorInt
    private val loseColor: Int = ContextCompat.getColor(context, R.color.lose)

    @ColorInt
    private val winColor: Int = ContextCompat.getColor(context, R.color.win)

    var listener: Listener? = null

    init {
        setOnClickListener(this)

        if (isInEditMode) {
            setContent(HeadToHeadMatch(MatchResult.WIN, LitePlayer("0", "Shroomed"),
                    LitePlayer("1", "PewPewU")))
        }
    }

    override fun clear() {
        super.clear()
        playerName.clear()
        opponentName.clear()
    }

    override fun onClick(v: View) {
        listener?.onClick(this)
    }

    override fun refresh() {
        super.refresh()

        val match = _headToHeadMatch

        if (match == null) {
            clear()
            return
        }

        playerName.text = match.player.name
        opponentName.text = match.opponent.name

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

    override fun setContent(content: HeadToHeadMatch) {
        _headToHeadMatch = content
        refresh()
    }

    interface Listener {
        fun onClick(v: HeadToHeadMatchItemView)
    }

}

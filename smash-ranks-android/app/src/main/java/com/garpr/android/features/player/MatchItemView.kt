package com.garpr.android.features.player

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.garpr.android.R
import com.garpr.android.data.models.MatchResult
import com.garpr.android.data.models.TournamentMatch
import com.garpr.android.extensions.getAttrColor
import kotlinx.android.synthetic.main.item_match.view.*

class MatchItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : FrameLayout(context, attrs), View.OnClickListener, View.OnLongClickListener {

    private val originalBackground: Drawable? = background

    @ColorInt
    private val cardBackgroundColor: Int = ContextCompat.getColor(context, R.color.card_background)

    @ColorInt
    private val exclusionColor: Int = context.getAttrColor(android.R.attr.textColorSecondary)

    @ColorInt
    private val loseColor: Int = ContextCompat.getColor(context, R.color.lose)

    @ColorInt
    private val winColor: Int = ContextCompat.getColor(context, R.color.win)

    var listeners: Listeners? = null
    private var _match: TournamentMatch? = null

    val match: TournamentMatch
        get() = checkNotNull(_match)

    interface Listeners {
        fun onClick(v: MatchItemView)
        fun onLongClick(v: MatchItemView)
    }

    init {
        setOnClickListener(this)
        setOnLongClickListener(this)
    }

    override fun onClick(v: View) {
        listeners?.onClick(this)
    }

    override fun onLongClick(v: View): Boolean {
        listeners?.onLongClick(this)
        return true
    }

    fun setContent(match: TournamentMatch, isIdentity: Boolean) {
        _match = match
        name.text = match.opponent.name

        name.setTextColor(when (match.result) {
            MatchResult.EXCLUDED -> exclusionColor
            MatchResult.LOSE -> loseColor
            MatchResult.WIN -> winColor
        })

        if (isIdentity) {
            name.typeface = Typeface.DEFAULT_BOLD
            setBackgroundColor(cardBackgroundColor)
        } else {
            name.typeface = Typeface.DEFAULT
            ViewCompat.setBackground(this, originalBackground)
        }
    }

}

package com.garpr.android.features.player

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.garpr.android.R
import com.garpr.android.data.models.Match
import com.garpr.android.data.models.MatchResult
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.features.common.adapters.BaseAdapterView
import com.garpr.android.features.common.views.LifecycleFrameLayout
import com.garpr.android.misc.Refreshable
import com.garpr.android.repositories.IdentityRepository
import kotlinx.android.synthetic.main.item_match.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class MatchItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleFrameLayout(context, attrs), BaseAdapterView<Match>,
        IdentityRepository.OnIdentityChangeListener, KoinComponent, Refreshable,
        View.OnClickListener, View.OnLongClickListener {

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
    private var _match: Match? = null

    val match: Match
        get() = checkNotNull(_match)

    protected val identityRepository: IdentityRepository by inject()

    interface Listeners {
        fun onClick(v: MatchItemView)
        fun onLongClick(v: MatchItemView)
    }

    init {
        setOnClickListener(this)
        setOnLongClickListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        identityRepository.addListener(this)
        refresh()
    }

    override fun onClick(v: View) {
        listeners?.onClick(this)
    }

    override fun onDetachedFromWindow() {
        identityRepository.removeListener(this)
        super.onDetachedFromWindow()
    }

    override fun onIdentityChange(identityRepository: IdentityRepository) {
        if (isAlive) {
            refresh()
        }
    }

    override fun onLongClick(v: View): Boolean {
        listeners?.onLongClick(this)
        return true
    }

    override fun refresh() {
        val match = _match

        if (match == null) {
            name.clear()
            return
        }

        if (identityRepository.isPlayer(match.opponent)) {
            name.typeface = Typeface.DEFAULT_BOLD
            setBackgroundColor(cardBackgroundColor)
        } else {
            name.typeface = Typeface.DEFAULT
            ViewCompat.setBackground(this, originalBackground)
        }

        name.text = match.opponent.name

        name.setTextColor(when (match.result) {
            MatchResult.EXCLUDED -> exclusionColor
            MatchResult.LOSE -> loseColor
            MatchResult.WIN -> winColor
        })
    }

    override fun setContent(content: Match) {
        _match = content
        refresh()
    }

}

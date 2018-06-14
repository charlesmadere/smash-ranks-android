package com.garpr.android.views

import android.content.Context
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.activities.PlayerActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.managers.RegionManager
import com.garpr.android.models.HeadToHeadMatch
import com.garpr.android.models.LitePlayer
import com.garpr.android.models.MatchResult
import kotterknife.bindView
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

    private val opponentName: TextView by bindView(R.id.tvOpponentName)
    private val playerName: TextView by bindView(R.id.tvPlayerName)


    override fun clear() {
        super.clear()

        playerName.clear()
        opponentName.clear()
        refresh()
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
        val items = arrayOf(match.player.name, match.opponent.name)

        AlertDialog.Builder(context)
                .setItems(items) { dialog, which ->
                    when (which) {
                        0 -> context.startActivity(PlayerActivity.getLaunchIntent(context,
                                match.player, regionManager.getRegion(context)))

                        1 -> context.startActivity(PlayerActivity.getLaunchIntent(context,
                                match.opponent, regionManager.getRegion(context)))

                        else -> throw RuntimeException("illegal which: $which")
                    }
                }
                .setTitle(R.string.view)
                .show()
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

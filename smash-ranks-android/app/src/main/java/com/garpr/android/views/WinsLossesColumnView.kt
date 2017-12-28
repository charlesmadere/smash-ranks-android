package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleableRes
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.activities.PlayerActivity
import com.garpr.android.extensions.clear
import com.garpr.android.misc.RegionManager
import com.garpr.android.models.WinsLosses
import kotterknife.bindView
import java.text.NumberFormat
import javax.inject.Inject

class WinsLossesColumnView : LinearLayout, View.OnClickListener {

    private val numberFormat = NumberFormat.getIntegerInstance()
    private lateinit var playerOrOpponent: PlayerOrOpponent

    private val playerName: TextView by bindView(R.id.playerName)
    private val winCount: TextView by bindView(R.id.winCount)

    @Inject
    protected lateinit var regionManager: RegionManager


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        parseAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        parseAttributes(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleableRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        parseAttributes(attrs)
    }

    private fun clear() {
        winCount.clear()
        playerName.clear()
    }

    override fun onClick(v: View) {
        val winsLosses = this.winsLosses ?: return

        val player = when (playerOrOpponent) {
            PlayerOrOpponent.PLAYER -> { winsLosses.player }
            PlayerOrOpponent.OPPONENT -> { winsLosses.opponent }
        }

        context.startActivity(PlayerActivity.getLaunchIntent(context, player,
                regionManager.getRegion(context)))
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
        }

        setOnClickListener(this)
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.WinsLossesColumnView)
        val playerOrOpponent = ta.getInt(R.styleable.WinsLossesColumnView_playerOrOpponent,
                -1)
        ta.recycle()

        if (playerOrOpponent < 0 || playerOrOpponent >= PlayerOrOpponent.values().size) {
            throw RuntimeException("playerOrOpponent attribute is an illegal value: $playerOrOpponent")
        }

        this.playerOrOpponent = PlayerOrOpponent.values()[playerOrOpponent]
    }

    internal var winsLosses: WinsLosses? = null
        set(value) {
            field = value

            if (value == null) {
                clear()
                return
            }

            when (playerOrOpponent) {
                PlayerOrOpponent.PLAYER -> {
                    winCount.text = numberFormat.format(value.playerWins)
                    playerName.text = value.player.name
                }

                PlayerOrOpponent.OPPONENT -> {
                    winCount.text = numberFormat.format(value.opponentWins)
                    playerName.text = value.opponent.name
                }
            }
        }

    private enum class PlayerOrOpponent {
        PLAYER, OPPONENT
    }

}

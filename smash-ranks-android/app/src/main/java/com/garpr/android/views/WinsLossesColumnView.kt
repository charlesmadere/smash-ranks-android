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
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.misc.RegionManager
import com.garpr.android.models.WinsLosses
import kotterknife.bindView
import java.text.NumberFormat
import javax.inject.Inject

class WinsLossesColumnView : LinearLayout, BaseAdapterView<WinsLosses>, View.OnClickListener {

    private var mContent: WinsLosses? = null
    private val mNumberFormat = NumberFormat.getIntegerInstance()
    private lateinit var mPlayerOrOpponent: PlayerOrOpponent

    private val mPlayerName: TextView by bindView(R.id.playerName)
    private val mWinCount: TextView by bindView(R.id.winCount)

    @Inject
    protected lateinit var mRegionManager: RegionManager


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

    override fun onClick(v: View) {
        val content = mContent ?: return

        val player = when (mPlayerOrOpponent) {
            PlayerOrOpponent.PLAYER -> { content.player }
            PlayerOrOpponent.OPPONENT -> { content.opponent }
        }

        context.startActivity(PlayerActivity.getLaunchIntent(context, player,
                mRegionManager.getRegion(context)))
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

        mPlayerOrOpponent = PlayerOrOpponent.values()[playerOrOpponent]
    }

    override fun setContent(content: WinsLosses) {
        mContent = content

        when (mPlayerOrOpponent) {
            PlayerOrOpponent.PLAYER -> {
                mWinCount.text = mNumberFormat.format(content.playerWins)
                mPlayerName.text = content.player.name
            }

            PlayerOrOpponent.OPPONENT -> {
                mWinCount.text = mNumberFormat.format(content.opponentWins)
                mPlayerName.text = content.opponent.name
            }
        }
    }

    private enum class PlayerOrOpponent {
        PLAYER, OPPONENT
    }

}

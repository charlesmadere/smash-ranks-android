package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.activities.PlayerActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.misc.RegionManager
import com.garpr.android.models.HeadToHeadMatch
import com.garpr.android.models.LitePlayer
import com.garpr.android.models.MatchResult
import kotterknife.bindView
import javax.inject.Inject

class HeadToHeadMatchItemView : IdentityFrameLayout, BaseAdapterView<HeadToHeadMatch>,
        View.OnClickListener {

    @Inject
    protected lateinit var regionManager: RegionManager

    private val opponentName: TextView by bindView(R.id.tvOpponentName)
    private val playerName: TextView by bindView(R.id.tvPlayerName)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun clear() {
        clearIdentity()
        playerName.clear()
        opponentName.clear()
        refreshIdentity()
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
                    playerName.setTextColor(context.getAttrColor(android.R.attr.textColorSecondary))
                    opponentName.setTextColor(context.getAttrColor(android.R.attr.textColorSecondary))
                }

                MatchResult.LOSE -> {
                    playerName.setTextColor(ContextCompat.getColor(context, R.color.lose))
                    opponentName.setTextColor(ContextCompat.getColor(context, R.color.win))
                }

                MatchResult.WIN -> {
                    playerName.setTextColor(ContextCompat.getColor(context, R.color.win))
                    opponentName.setTextColor(ContextCompat.getColor(context, R.color.lose))
                }
            }
        }

    override fun onClick(v: View) {
        val match = this.match ?: return
        val items = arrayOf(match.player.name, match.opponent.name)

        AlertDialog.Builder(context)
                .setItems(items, { dialog, which ->
                    when (which) {
                        0 -> context.startActivity(PlayerActivity.getLaunchIntent(context,
                                match.player, regionManager.getRegion(context)))

                        1 -> context.startActivity(PlayerActivity.getLaunchIntent(context,
                                match.opponent, regionManager.getRegion(context)))

                        else -> throw RuntimeException("illegal which: $which")
                    }
                })
                .setTitle(R.string.view)
                .show()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
        }

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

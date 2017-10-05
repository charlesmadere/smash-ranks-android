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
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.misc.RegionManager
import com.garpr.android.models.HeadToHeadMatch
import com.garpr.android.models.LitePlayer
import com.garpr.android.models.MatchResult
import kotterknife.bindView
import javax.inject.Inject

class HeadToHeadMatchItemView : IdentityFrameLayout, BaseAdapterView<HeadToHeadMatch>,
        View.OnClickListener {

    private var mContent: HeadToHeadMatch? = null

    @Inject
    protected lateinit var mRegionManager: RegionManager

    private val mPlayerName: TextView by bindView(R.id.tvPlayerName)
    private val mOpponentName: TextView by bindView(R.id.tvOpponentName)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onClick(v: View) {
        val content = mContent ?: return
        val items = arrayOf(content.player.name, content.opponent.name)

        AlertDialog.Builder(context)
                .setItems(items, { dialog, which ->
                    when (which) {
                        0 -> context.startActivity(PlayerActivity.getLaunchIntent(context,
                                content.player, mRegionManager.getRegion(context)))

                        1 -> context.startActivity(PlayerActivity.getLaunchIntent(context,
                                content.opponent, mRegionManager.getRegion(context)))

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

        if (isInEditMode) {
            setContent(HeadToHeadMatch(LitePlayer("0", "Shroomed"),
                    LitePlayer("1", "PewPewU"), MatchResult.WIN))
        }
    }

    override fun setContent(content: HeadToHeadMatch) {
        mContent = content

        mPlayerName.text = content.player.name
        mOpponentName.text = content.opponent.name

        when (content.result) {
            MatchResult.EXCLUDED -> {
                mPlayerName.setTextColor(context.getAttrColor(android.R.attr.textColorSecondary))
                mOpponentName.setTextColor(context.getAttrColor(android.R.attr.textColorSecondary))
            }

            MatchResult.LOSE -> {
                mPlayerName.setTextColor(ContextCompat.getColor(context, R.color.lose))
                mOpponentName.setTextColor(ContextCompat.getColor(context, R.color.win))
            }

            MatchResult.WIN -> {
                mPlayerName.setTextColor(ContextCompat.getColor(context, R.color.win))
                mOpponentName.setTextColor(ContextCompat.getColor(context, R.color.lose))
            }
        }
    }

}

package com.garpr.android.views

import android.content.Context
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.activities.HeadToHeadActivity
import com.garpr.android.activities.PlayerActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.managers.RegionManager
import com.garpr.android.models.FullTournament
import kotterknife.bindView
import javax.inject.Inject

class TournamentMatchItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : IdentityConstraintLayout(context, attrs), BaseAdapterView<FullTournament.Match>,
        View.OnClickListener {

    @ColorInt
    private var exclusionColor: Int = 0

    @ColorInt
    private var loseColor: Int = 0

    @ColorInt
    private var winColor: Int = 0

    @Inject
    protected lateinit var regionManager: RegionManager

    private val loserName: TextView by bindView(R.id.tvLoserName)
    private val winnerName: TextView by bindView(R.id.tvWinnerName)


    override fun clear() {
        super.clear()

        loserName.clear()
        winnerName.clear()
        refresh()
    }

    private var match: FullTournament.Match? = null
        set(value) {
            field = value

            if (value == null) {
                clear()
                return
            }

            loserName.text = value.loserName
            winnerName.text = value.winnerName

            if (value.isExcluded) {
                loserName.setTextColor(exclusionColor)
                winnerName.setTextColor(exclusionColor)
            } else {
                loserName.setTextColor(loseColor)
                winnerName.setTextColor(winColor)
            }

            refresh()
        }

    override fun onClick(v: View) {
        val match = this.match ?: return
        val items = arrayOf(match.winnerName, match.loserName,
                resources.getText(R.string.head_to_head))

        AlertDialog.Builder(context)
                .setItems(items) { dialog, which ->
                    when (which) {
                        0 -> context.startActivity(PlayerActivity.getLaunchIntent(context,match.winnerId,
                                    regionManager.getRegion(context)))

                        1 -> context.startActivity(PlayerActivity.getLaunchIntent(context, match.loserId,
                                regionManager.getRegion(context)))

                        2 -> context.startActivity(HeadToHeadActivity.getLaunchIntent(context, match,
                                regionManager.getRegion(context)))

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
    }

    override fun refresh() {
        val match = this.match

        if (match != null && identityManager.isPlayer(match.winnerId)) {
            styleTextViewForUser(winnerName)
            styleTextViewForSomeoneElse(loserName)
            identityIsUser()
        } else if (match != null && identityManager.isPlayer(match.loserId)) {
            styleTextViewForSomeoneElse(winnerName)
            styleTextViewForUser(loserName)
            identityIsUser()
        } else {
            styleTextViewForSomeoneElse(winnerName)
            styleTextViewForSomeoneElse(loserName)
            identityIsSomeoneElse()
        }
    }

    override fun setContent(content: FullTournament.Match) {
        match = content
    }

}

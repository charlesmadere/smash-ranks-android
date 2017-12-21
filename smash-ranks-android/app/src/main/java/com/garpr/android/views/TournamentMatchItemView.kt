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
import com.garpr.android.activities.HeadToHeadActivity
import com.garpr.android.activities.PlayerActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.misc.RegionManager
import com.garpr.android.models.FullTournament
import kotterknife.bindView
import javax.inject.Inject

class TournamentMatchItemView : IdentityFrameLayout, BaseAdapterView<FullTournament.Match>,
        View.OnClickListener {

    @Inject
    protected lateinit var regionManager: RegionManager

    private val loserName: TextView by bindView(R.id.tvLoserName)
    private val winnerName: TextView by bindView(R.id.tvWinnerName)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun clear() {
        clearIdentity()
        loserName.clear()
        winnerName.clear()
        refreshIdentity()
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
                loserName.setTextColor(context.getAttrColor(android.R.attr.textColorSecondary))
                winnerName.setTextColor(context.getAttrColor(android.R.attr.textColorSecondary))
            } else {
                loserName.setTextColor(ContextCompat.getColor(context, R.color.lose))
                winnerName.setTextColor(ContextCompat.getColor(context, R.color.win))
            }

            refreshIdentity()
        }

    override fun onClick(v: View) {
        val match = this.match ?: return
        val items = arrayOf(match.winnerName, match.loserName,
                resources.getText(R.string.head_to_head))

        AlertDialog.Builder(context)
                .setItems(items, { dialog, which ->
                    when (which) {
                        0 -> context.startActivity(PlayerActivity.getLaunchIntent(context,
                                match.winnerId, match.winnerName, regionManager.getRegion(context)))

                        1 -> context.startActivity(PlayerActivity.getLaunchIntent(context,
                                match.loserId, match.loserName, regionManager.getRegion(context)))

                        2 -> context.startActivity(HeadToHeadActivity.getLaunchIntent(context,
                                match, regionManager.getRegion(context)))

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
    }

    override fun refreshIdentity() {
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

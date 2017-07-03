package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.activities.HeadToHeadActivity
import com.garpr.android.activities.PlayerActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.misc.RegionManager
import com.garpr.android.models.FullTournament
import kotterknife.bindView
import javax.inject.Inject

class TournamentMatchItemView : IdentityFrameLayout, BaseAdapterView<FullTournament.Match>,
        DialogInterface.OnClickListener, View.OnClickListener {

    private var mContent: FullTournament.Match? = null

    @Inject
    lateinit protected var mRegionManager: RegionManager

    private val mLoserName: TextView by bindView(R.id.tvLoserName)
    private val mWinnerName: TextView by bindView(R.id.tvWinnerName)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onClick(dialog: DialogInterface, which: Int) {
        when (which) {
            0 -> context.startActivity(PlayerActivity.getLaunchIntent(context,
                    mContent!!.winnerId, mContent!!.winnerName,
                    mRegionManager.getRegion(context)))

            1 -> context.startActivity(PlayerActivity.getLaunchIntent(context,
                    mContent!!.loserId, mContent!!.loserName,
                    mRegionManager.getRegion(context)))

            2 -> context.startActivity(HeadToHeadActivity.getLaunchIntent(context,
                    mContent!!))

            else -> throw RuntimeException("illegal which: " + which)
        }
    }

    override fun onClick(v: View) {
        val items = arrayOf(mContent!!.winnerName, mContent!!.loserName,
                resources.getText(R.string.head_to_head))

        AlertDialog.Builder(context)
                .setItems(items, this)
                .setTitle(R.string.view)
                .show()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        App.get().appComponent.inject(this)
        setOnClickListener(this)
    }

    override fun refreshIdentity() {
        if (mIdentityManager.isId(mContent!!.winnerId)) {
            styleTextViewForUser(mWinnerName)
            styleTextViewForSomeoneElse(mLoserName)
            identityIsUser()
        } else if (mIdentityManager.isId(mContent!!.loserId)) {
            styleTextViewForSomeoneElse(mWinnerName)
            styleTextViewForUser(mLoserName)
            identityIsUser()
        } else {
            styleTextViewForSomeoneElse(mWinnerName)
            styleTextViewForSomeoneElse(mLoserName)
            identityIsSomeoneElse()
        }
    }

    override fun setContent(content: FullTournament.Match) {
        mContent = content
        mLoserName.text = content.loserName
        mWinnerName.text = content.winnerName
        refreshIdentity()
    }

}

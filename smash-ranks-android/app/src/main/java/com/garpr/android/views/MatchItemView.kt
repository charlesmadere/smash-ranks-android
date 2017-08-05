package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.activities.PlayerActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.optActivity
import com.garpr.android.misc.FavoritePlayersManager
import com.garpr.android.misc.RegionManager
import com.garpr.android.models.Match
import kotterknife.bindView
import javax.inject.Inject

class MatchItemView : IdentityFrameLayout, BaseAdapterView<Match>, View.OnClickListener,
        View.OnLongClickListener {

    var mContent: Match? = null
        private set

    @Inject
    lateinit protected var mFavoritePlayersManager: FavoritePlayersManager

    @Inject
    lateinit protected var mRegionManager: RegionManager

    private val mName: TextView by bindView(R.id.tvName)


    interface OnClickListener {
        fun onClick(v: MatchItemView)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun identityIsSomeoneElse() {
        super.identityIsSomeoneElse()
        styleTextViewForSomeoneElse(mName)
    }

    override fun identityIsUser() {
        super.identityIsUser()
        styleTextViewForUser(mName)
    }

    override fun onClick(v: View) {
        val content = mContent ?: return
        val activity = context.optActivity()

        if (activity is OnClickListener) {
            activity.onClick(this)
        } else {
            val opponent = content.opponent
            context.startActivity(PlayerActivity.getLaunchIntent(context,
                    opponent.id, opponent.name, mRegionManager.getRegion(context)))
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        App.get().appComponent.inject(this)
        setOnClickListener(this)
        setOnLongClickListener(this)
    }

    override fun onLongClick(v: View): Boolean {
        val content = mContent ?: return false
        return mFavoritePlayersManager.showAddOrRemovePlayerDialog(context, content.opponent,
                mRegionManager.getRegion(context))
    }

    override fun setContent(content: Match) {
        mContent = content
        mName.text = content.opponent.name

        when (content.result) {
            Match.Result.EXCLUDED -> mName.setTextColor(context.getAttrColor(android.R.attr.textColorSecondary))
            Match.Result.LOSE -> mName.setTextColor(ContextCompat.getColor(context, R.color.lose))
            Match.Result.WIN -> mName.setTextColor(ContextCompat.getColor(context, R.color.win))
        }

        refreshIdentity()
    }

}

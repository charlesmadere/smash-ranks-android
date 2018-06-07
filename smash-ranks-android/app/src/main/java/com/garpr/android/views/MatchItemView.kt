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
import com.garpr.android.R
import com.garpr.android.activities.PlayerActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.managers.FavoritePlayersManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.models.Match
import com.garpr.android.models.MatchResult
import kotterknife.bindView
import javax.inject.Inject

class MatchItemView : IdentityFrameLayout, BaseAdapterView<Match>, View.OnClickListener,
        View.OnLongClickListener {

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var regionManager: RegionManager

    private val name: TextView by bindView(R.id.tvName)


    interface OnClickListener {
        fun onClick(v: MatchItemView)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun clear() {
        super.clear()

        name.clear()
        refresh()
    }

    override fun identityIsSomeoneElse() {
        super.identityIsSomeoneElse()
        styleTextViewForSomeoneElse(name)
    }

    override fun identityIsUser() {
        super.identityIsUser()
        styleTextViewForUser(name)
    }

    var match: Match? = null
        private set(value) {
            field = value

            if (value == null) {
                clear()
                return
            }

            identity = value.opponent
            name.text = value.opponent.name

            when (value.result) {
                MatchResult.EXCLUDED -> name.setTextColor(context.getAttrColor(android.R.attr.textColorSecondary))
                MatchResult.LOSE -> name.setTextColor(ContextCompat.getColor(context, R.color.lose))
                MatchResult.WIN -> name.setTextColor(ContextCompat.getColor(context, R.color.win))
            }

            refresh()
        }

    override fun onClick(v: View) {
        val match = this.match ?: return
        val activity = this.activity

        if (activity is OnClickListener) {
            activity.onClick(this)
        } else {
            val opponent = match.opponent
            context.startActivity(PlayerActivity.getLaunchIntent(context, opponent.id,
                    regionManager.getRegion(context)))
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
        }

        setOnClickListener(this)
        setOnLongClickListener(this)
    }

    override fun onLongClick(v: View): Boolean {
        return favoritePlayersManager.showAddOrRemovePlayerDialog(context, match?.opponent,
                regionManager.getRegion(context))
    }

    override fun setContent(content: Match) {
        match = content
    }

}

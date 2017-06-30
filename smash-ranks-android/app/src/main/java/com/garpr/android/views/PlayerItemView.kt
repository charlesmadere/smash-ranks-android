package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.activities.PlayerActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.misc.FavoritePlayersManager
import com.garpr.android.misc.RegionManager
import com.garpr.android.models.AbsPlayer
import kotterknife.bindView
import javax.inject.Inject

class PlayerItemView : IdentityFrameLayout, BaseAdapterView<AbsPlayer>, View.OnClickListener,
        View.OnLongClickListener {

    @Inject
    lateinit protected var mFavoritePlayersManager: FavoritePlayersManager

    @Inject
    lateinit protected var mRegionManager: RegionManager

    private val mName: TextView by bindView(R.id.tvName)


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
        val identity = mIdentity ?: return
        context.startActivity(PlayerActivity.getLaunchIntent(context, identity,
                mRegionManager.getRegion(context)))
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
        return mFavoritePlayersManager.showAddOrRemovePlayerDialog(context, mIdentity,
                mRegionManager.getRegion(context))
    }

    override fun setContent(content: AbsPlayer) {
        mIdentity = content
        mName.text = content.name
        refreshIdentity()
    }

}

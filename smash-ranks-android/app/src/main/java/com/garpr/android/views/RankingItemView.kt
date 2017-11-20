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
import com.garpr.android.misc.*
import com.garpr.android.models.RankedPlayer
import kotterknife.bindOptionalView
import kotterknife.bindView
import java.text.NumberFormat
import javax.inject.Inject

class RankingItemView : IdentityFrameLayout, BaseAdapterView<RankedPlayer>,
        View.OnClickListener, View.OnLongClickListener {

    private val mNumberFormat = NumberFormat.getIntegerInstance()

    @Inject
    protected lateinit var mFavoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var mPreviousRankUtils: PreviousRankUtils

    @Inject
    protected lateinit var mRegionManager: RegionManager

    @Inject
    protected lateinit var mTimber: Timber

    private val mPreviousRankView: PreviousRankView? by bindOptionalView(R.id.previousRankView)
    private val mName: TextView by bindView(R.id.tvName)
    private val mRank: TextView by bindView(R.id.tvRank)
    private val mRating: TextView by bindView(R.id.tvRating)


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
        val content = mIdentity ?: return
        context.startActivity(PlayerActivity.getLaunchIntent(context, content,
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

    override fun setContent(content: RankedPlayer) {
        mIdentity = content

        mPreviousRankView?.setContent(mPreviousRankUtils.getRankInfo(content))
        mRank.text = mNumberFormat.format(content.rank)
        mName.text = content.name
        mRating.text = MiscUtils.truncateFloat(content.rating)

        refreshIdentity()
    }

}

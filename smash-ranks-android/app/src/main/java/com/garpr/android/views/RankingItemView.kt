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
import com.garpr.android.models.Ranking
import kotterknife.bindOptionalView
import kotterknife.bindView
import java.text.NumberFormat
import javax.inject.Inject

class RankingItemView : IdentityFrameLayout, BaseAdapterView<Ranking>, View.OnClickListener,
        View.OnLongClickListener {

    private val mNumberFormat: NumberFormat = NumberFormat.getNumberInstance()
    private var mContent: Ranking? = null

    @Inject
    lateinit protected var mFavoritePlayersManager: FavoritePlayersManager

    @Inject
    lateinit protected var mRegionManager: RegionManager

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
        val content = mContent ?: return
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
        val content = mContent ?: return false
        return mFavoritePlayersManager.showAddOrRemovePlayerDialog(context, content.player,
                mRegionManager.getRegion(context))
    }

    override fun setContent(content: Ranking) {
        mContent = content

        mPreviousRankView?.setContent(content)
        mRank.text = mNumberFormat.format(content.rank.toLong())
        mName.text = content.name
        mRating.text = content.ratingTruncated

        refreshIdentity()
    }

}

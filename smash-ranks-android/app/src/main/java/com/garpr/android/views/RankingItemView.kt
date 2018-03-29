package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.activities.PlayerActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.managers.FavoritePlayersManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.MiscUtils
import com.garpr.android.misc.PreviousRankUtils
import com.garpr.android.misc.Timber
import com.garpr.android.models.RankedPlayer
import kotterknife.bindOptionalView
import kotterknife.bindView
import java.text.NumberFormat
import javax.inject.Inject

class RankingItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : IdentityConstraintLayout(context, attrs), BaseAdapterView<RankedPlayer>,
        View.OnClickListener, View.OnLongClickListener {

    private val numberFormat = NumberFormat.getIntegerInstance()

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    @Inject
    protected lateinit var previousRankUtils: PreviousRankUtils

    @Inject
    protected lateinit var regionManager: RegionManager

    @Inject
    protected lateinit var timber: Timber

    private val previousRankView: PreviousRankView? by bindOptionalView(R.id.previousRankView)
    private val name: TextView by bindView(R.id.tvName)
    private val rank: TextView by bindView(R.id.tvRank)
    private val rating: TextView by bindView(R.id.tvRating)


    override fun identityIsSomeoneElse() {
        super.identityIsSomeoneElse()
        styleTextViewForSomeoneElse(name)
    }

    override fun identityIsUser() {
        super.identityIsUser()
        styleTextViewForUser(name)
    }

    override fun onClick(v: View) {
        val identity = this.identity ?: return
        context.startActivity(PlayerActivity.getLaunchIntent(context, identity,
                regionManager.getRegion(context)))
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
        }

        setOnClickListener(this)
        setOnLongClickListener(this)
    }

    override fun onLongClick(v: View): Boolean {
        return favoritePlayersManager.showAddOrRemovePlayerDialog(context, identity,
                regionManager.getRegion(context))
    }

    override fun setContent(content: RankedPlayer) {
        identity = content

        previousRankView?.setContent(previousRankUtils.getRankInfo(content))
        rank.text = numberFormat.format(content.rank)
        name.text = content.name
        rating.text = MiscUtils.truncateFloat(content.rating)

        refresh()
    }

}

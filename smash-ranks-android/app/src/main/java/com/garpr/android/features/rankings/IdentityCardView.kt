package com.garpr.android.features.rankings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.garpr.android.R
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.PreviousRank
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_identity_card.view.*

class IdentityCardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : MaterialCardView(context, attrs), View.OnClickListener {

    private var _identity: FavoritePlayer? = null

    val identity: FavoritePlayer
        get() = checkNotNull(_identity)

    var listener: Listener? = null

    interface Listener {
        fun onClick(v: IdentityCardView)
    }

    init {
        setOnClickListener(this)
    }

    override fun onClick(v: View) {
        listener?.onClick(this)
    }

    fun setContent(
            player: FavoritePlayer,
            previousRank: PreviousRank,
            avatar: String?,
            rank: String,
            rating: String,
            tag: String
    ) {
        _identity = player

        if (avatar.isNullOrBlank()) {
            avatarContainer.visibility = GONE
        } else {
            this.avatar.setImageURI(avatar)
            avatarContainer.visibility = VISIBLE
        }

        playerTag.text = tag

        when (previousRank) {
            PreviousRank.DECREASE -> {
                rankBadge.setBackgroundResource(R.drawable.identity_card_lose_badge)
                rankBadge.setImageResource(R.drawable.ic_arrow_downward_white_18dp)
            }

            PreviousRank.GONE, PreviousRank.INVISIBLE -> {
                rankBadge.setBackgroundResource(R.drawable.identity_card_neutral_badge)
                rankBadge.setImageResource(R.drawable.ic_trophy_white_18dp)
            }

            PreviousRank.INCREASE -> {
                rankBadge.setBackgroundResource(R.drawable.identity_card_win_badge)
                rankBadge.setImageResource(R.drawable.ic_arrow_upward_white_18dp)
            }
        }

        this.rank.text = rank
        this.rating.text = rating
    }

}

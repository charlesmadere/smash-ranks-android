package com.garpr.android.features.rankings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.PreviousRank
import com.garpr.android.extensions.setTintedImageResource
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_identity_card.view.*

class IdentityCardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : MaterialCardView(context, attrs), View.OnClickListener {

    private var _identity: FavoritePlayer? = null

    val identity: FavoritePlayer
        get() = checkNotNull(_identity)

    @ColorInt
    private val loseColor: Int = ContextCompat.getColor(context, R.color.lose)

    @ColorInt
    private val winColor: Int = ContextCompat.getColor(context, R.color.win)

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
            name: String,
            rank: String,
            rating: String
    ) {
        _identity = player

        if (avatar.isNullOrBlank()) {
            this.avatar.visibility = GONE
        } else {
            this.avatar.setImageURI(avatar)
            this.avatar.visibility = VISIBLE
        }

        this.name.text = name

        when (previousRank) {
            PreviousRank.DECREASE -> {
                this.previousRank.setTintedImageResource(R.drawable.ic_arrow_downward_white_18dp, loseColor)
                this.previousRank.visibility = VISIBLE
            }

            PreviousRank.GONE -> {
                this.previousRank.visibility = GONE
            }

            PreviousRank.INCREASE -> {
                this.previousRank.setTintedImageResource(R.drawable.ic_arrow_upward_white_18dp, winColor)
                this.previousRank.visibility = VISIBLE
            }

            PreviousRank.INVISIBLE -> {
                this.previousRank.visibility = INVISIBLE
            }
        }

        this.rank.text = rank
        this.rating.text = rating
    }

}

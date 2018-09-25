package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.extensions.setTintedImageResource
import com.garpr.android.misc.PreviousRankUtils

class PreviousRankView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs), BaseAdapterView<PreviousRankUtils.Info?> {

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            setContent(PreviousRankUtils.Info.INCREASE)
        }
    }

    override fun setContent(content: PreviousRankUtils.Info?) {
        if (content == null) {
            setImageDrawable(null)
            return
        }

        @DrawableRes val drawableResId: Int
        @ColorRes val tintResId: Int

        when (content) {
            PreviousRankUtils.Info.DECREASE -> {
                drawableResId = R.drawable.ic_arrow_downward_white_18dp
                tintResId = R.color.lose
            }

            PreviousRankUtils.Info.INCREASE -> {
                drawableResId = R.drawable.ic_arrow_upward_white_18dp
                tintResId = R.color.win
            }
        }

        setTintedImageResource(drawableResId, ContextCompat.getColor(context, tintResId))
    }

}

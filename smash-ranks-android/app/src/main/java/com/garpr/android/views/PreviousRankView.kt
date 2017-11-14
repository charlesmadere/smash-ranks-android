package com.garpr.android.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.AttrRes
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.misc.PreviousRankUtils

class PreviousRankView : AppCompatImageView, BaseAdapterView<PreviousRankUtils.Info?> {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    private fun createDrawable(@DrawableRes drawableResId: Int, @ColorRes tintResId: Int): Drawable? {
        return ContextCompat.getDrawable(context, drawableResId)?.let {
            val drawable = DrawableCompat.wrap(it)
            DrawableCompat.setTint(drawable, ContextCompat.getColor(context, tintResId))
            drawable
        }
    }

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

        setImageDrawable(createDrawable(drawableResId, tintResId))
    }

}

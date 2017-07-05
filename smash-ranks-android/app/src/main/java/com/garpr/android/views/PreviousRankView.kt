package com.garpr.android.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.misc.PreviousRankUtils
import com.garpr.android.models.Ranking
import javax.inject.Inject

class PreviousRankView : AppCompatImageView, BaseAdapterView<Ranking> {

    @Inject
    lateinit protected var mPreviousRankUtils: PreviousRankUtils


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context,
            attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        App.get().appComponent.inject(this)
    }

    override fun setContent(content: Ranking) {
        val info = mPreviousRankUtils.checkRanking(content)

        if (info == null) {
            setImageDrawable(null)
            visibility = INVISIBLE
            return
        }

        val drawableResId: Int
        val tintResId: Int

        when (info) {
            PreviousRankUtils.Info.DECREASE -> {
                drawableResId = R.drawable.ic_arrow_downward_white_18dp
                tintResId = R.color.lose
            }

            PreviousRankUtils.Info.INCREASE -> {
                drawableResId = R.drawable.ic_arrow_upward_white_18dp
                tintResId = R.color.win
            }
        }

        val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, drawableResId))
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, tintResId))
        setImageDrawable(drawable)
        visibility = VISIBLE
    }

}

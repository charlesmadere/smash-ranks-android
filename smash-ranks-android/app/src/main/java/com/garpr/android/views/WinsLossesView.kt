package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleableRes
import android.util.AttributeSet
import android.widget.LinearLayout
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.models.LitePlayer
import com.garpr.android.models.WinsLosses
import kotterknife.bindView

class WinsLossesView : LinearLayout, BaseAdapterView<WinsLosses> {

    private var mContent: WinsLosses? = null
    private val mPlayerWinsRect = RectF()
    private val mOpponentWinsRect = RectF()

    private val mPlayerColumnView: WinsLossesColumnView by bindView(R.id.playerColumnView)
    private val mOpponentColumnView: WinsLossesColumnView by bindView(R.id.opponentColumnView)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleableRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun calculateRects() {
        val h = height
        val w = width
        val c = mContent

        if (h == 0 || w == 0 || c == null) {
            mPlayerWinsRect.setEmpty()
            mOpponentWinsRect.setEmpty()
        }

        // TODO
    }

    override fun invalidate() {
        calculateRects()
        super.invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mPlayerWinsRect.isEmpty && mOpponentWinsRect.isEmpty) {
            return
        }

        // TODO
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            setContent(WinsLosses(LitePlayer("0", "PewPewU"), 8,
                    LitePlayer("1", "Shroomed"), 5))
        }
    }

    override fun setContent(content: WinsLosses) {
        mContent = content
        mPlayerColumnView.setContent(content)
        mOpponentColumnView.setContent(content)
        invalidate()
    }

}

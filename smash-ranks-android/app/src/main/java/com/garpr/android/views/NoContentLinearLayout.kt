package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.garpr.android.R
import kotterknife.bindView

open class NoContentLinearLayout : LinearLayout {

    private var mLine1Text: CharSequence? = null
    private var mLine2Text: CharSequence? = null

    private val mLine1: TextView by bindView(R.id.noContentLine1)
    private val mLine2: TextView by bindView(R.id.noContentLine2)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        parseAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        parseAttributes(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        parseAttributes(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        LayoutInflater.from(context).inflate(R.layout.no_content_linear_layout_body, this,
                true)

        setLine1Text(mLine1Text)
        setLine2Text(mLine2Text)
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.NoContentLinearLayout)
        mLine1Text = ta.getText(R.styleable.NoContentLinearLayout_line1Text)
        mLine2Text = ta.getText(R.styleable.NoContentLinearLayout_line2Text)
        ta.recycle()
    }

    fun setLine1Text(text: CharSequence?) {
        mLine1Text = text
        mLine1.text = text
    }

    fun setLine2Text(text: CharSequence?) {
        mLine2Text = text
        mLine2.text = text
    }

}

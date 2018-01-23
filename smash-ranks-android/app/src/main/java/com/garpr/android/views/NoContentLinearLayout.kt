package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.DrawableRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.garpr.android.R
import kotterknife.bindOptionalView
import kotterknife.bindView

open class NoContentLinearLayout : LinearLayout {

    @DrawableRes
    private var imageResId: Int = 0

    private var line1Text: CharSequence? = null
    private var line2Text: CharSequence? = null

    protected val image: ImageView? by bindOptionalView(R.id.noContentImage)
    protected val line1: TextView by bindView(R.id.noContentLine1)
    protected val line2: TextView by bindView(R.id.noContentLine2)


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

        val layoutInflater = LayoutInflater.from(context)

        if (imageResId == 0) {
            layoutInflater.inflate(R.layout.no_content_linear_layout_body, this,
                    true)
        } else {
            layoutInflater.inflate(R.layout.no_content_linear_layout_body_with_image, this,
                    true)
            image?.setImageResource(imageResId)
        }

        setLine1Text(line1Text)
        setLine2Text(line2Text)
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.NoContentLinearLayout)
        line1Text = ta.getText(R.styleable.NoContentLinearLayout_line1Text)
        line2Text = ta.getText(R.styleable.NoContentLinearLayout_line2Text)

        if (ta.hasValue(R.styleable.NoContentLinearLayout_imageSrc)) {
            imageResId = ta.getResourceId(R.styleable.NoContentLinearLayout_imageSrc, 0)
        }

        ta.recycle()
    }

    fun setLine1Text(text: CharSequence?) {
        line1Text = text
        line1.text = text
    }

    fun setLine2Text(text: CharSequence?) {
        line2Text = text
        line2.text = text
    }

}

package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.extensions.clear
import kotterknife.bindView

open class NoContentLinearLayout : LinearLayout {

    private var _descriptionText: CharSequence? = null
    private var _titleText: CharSequence? = null
    private var _imageDrawable: Drawable? = null

    private val image: ImageView by bindView(R.id.image)
    private val description: TextView by bindView(R.id.description)
    private val title: TextView by bindView(R.id.title)


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

    var descriptionText: CharSequence?
        get() = _descriptionText
        set(value) {
            _descriptionText = value
            description.text = value
        }

    var imageDrawable: Drawable?
        get() = _imageDrawable
        set(value) {
            _imageDrawable = value

            if (value == null) {
                image.clear()
                image.visibility = View.GONE
            } else {
                image.setImageDrawable(value)
                image.visibility = View.VISIBLE
            }
        }

    override fun onFinishInflate() {
        super.onFinishInflate()

        LayoutInflater.from(context).inflate(R.layout.no_content_linear_layout_body, this,
                true)

        titleText = _titleText
        descriptionText = _descriptionText
        imageDrawable = _imageDrawable
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.View)
        _titleText = ta.getText(R.styleable.View_titleText)
        _descriptionText = ta.getText(R.styleable.View_descriptionText)
        _imageDrawable = ta.getDrawable(R.styleable.View_image)
        ta.recycle()
    }

    var titleText: CharSequence?
        get() = _titleText
        set(value) {
            _titleText = value
            title.text = value
        }

}

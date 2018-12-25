package com.garpr.android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.garpr.android.R
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.layoutInflater
import kotlinx.android.synthetic.main.no_content_linear_layout_body.view.*

open class NoContentLinearLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    var descriptionText: CharSequence?
        get() = description.text
        set(value) {
            description.text = value
        }

    var imageDrawable: Drawable?
        get() = image.drawable
        set(value) {
            if (value == null) {
                image.clear()
                image.visibility = View.GONE
            } else {
                image.setImageDrawable(value)
                image.visibility = View.VISIBLE
            }
        }

    var titleText: CharSequence?
        get() = title.text
        set(value) {
            title.text = value
        }

    init {
        @Suppress("LeakingThis")
        layoutInflater.inflate(R.layout.no_content_linear_layout_body, this)

        @SuppressLint("CustomViewStyleable")
        val ta = context.obtainStyledAttributes(attrs, R.styleable.View)
        titleText = ta.getText(R.styleable.View_titleText)
        descriptionText = ta.getText(R.styleable.View_descriptionText)
        imageDrawable = ta.getDrawable(R.styleable.View_image)
        ta.recycle()
    }

}

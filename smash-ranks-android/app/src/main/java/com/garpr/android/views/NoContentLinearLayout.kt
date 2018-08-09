package com.garpr.android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.extensions.clear
import kotterknife.bindView

open class NoContentLinearLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val image: ImageView by bindView(R.id.image)
    private val description: TextView by bindView(R.id.description)
    private val title: TextView by bindView(R.id.title)

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
        LayoutInflater.from(context).inflate(R.layout.no_content_linear_layout_body, this,
                true)

        @SuppressLint("CustomViewStyleable")
        val ta = context.obtainStyledAttributes(attrs, R.styleable.View)
        titleText = ta.getText(R.styleable.View_titleText)
        descriptionText = ta.getText(R.styleable.View_descriptionText)
        imageDrawable = ta.getDrawable(R.styleable.View_image)
        ta.recycle()
    }

}

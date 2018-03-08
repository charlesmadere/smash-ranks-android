package com.garpr.android.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.setTintedImageDrawable
import com.garpr.android.misc.Refreshable
import kotterknife.bindView

open class SimplePreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleConstraintLayout(context, attrs), Refreshable {

    private var _descriptionText: CharSequence? = null
    private var _titleText: CharSequence? = null
    private var _iconDrawable: Drawable? = null

    private val icon: ImageView by bindView(R.id.checkable)
    private val description: TextView by bindView(R.id.description)
    private val title: TextView by bindView(R.id.title)


    init {
        parseAttributes(attrs)
    }

    var descriptionText: CharSequence?
        get() = _descriptionText
        set(value) {
            _descriptionText = value
            description.text = value
        }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    var iconDrawable: Drawable?
        get() = icon.drawable
        set(value) {
            _iconDrawable = value

            if (value == null) {
                icon.clear()
                icon.visibility = View.GONE
            } else {
                icon.setTintedImageDrawable(value,
                        context.getAttrColor(android.R.attr.textColorSecondary))
                icon.visibility = View.VISIBLE
            }
        }

    override fun onFinishInflate() {
        super.onFinishInflate()

        LayoutInflater.from(context).inflate(R.layout.view_simple_preference, this)

        descriptionText = _descriptionText
        iconDrawable = _iconDrawable
        titleText = _titleText
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.View)
        _descriptionText = ta.getText(R.styleable.View_descriptionText)
        _iconDrawable = ta.getDrawable(R.styleable.View_icon)
        _titleText = ta.getText(R.styleable.View_titleText)
        ta.recycle()
    }

    override fun refresh() {
        // intentionally empty, children can override
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        title.isEnabled = enabled
        description.isEnabled = enabled
    }

    var titleText: CharSequence?
        get() = _titleText
        set(value) {
            _titleText = value
            title.text = value
        }

}

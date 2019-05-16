package com.garpr.android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import com.garpr.android.R
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.setTintedImageDrawable
import com.garpr.android.misc.Refreshable
import kotlinx.android.synthetic.main.view_simple_preference.view.*

open class SimplePreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleConstraintLayout(context, attrs), Refreshable {

    var descriptionText: CharSequence?
        get() = description.text
        set(value) {
            description.text = value
        }

    var imageDrawable: Drawable?
        get() = icon.drawable
        set(value) {
            if (value == null) {
                icon.clear()
                icon.visibility = View.GONE
            } else {
                icon.setTintedImageDrawable(value,
                        context.getAttrColor(android.R.attr.textColorSecondary))
                icon.visibility = View.VISIBLE
            }
        }

    var titleText: CharSequence?
        get() = title.text
        set(value) {
            title.text = value
        }

    init {
        @Suppress("LeakingThis")
        layoutInflater.inflate(R.layout.view_simple_preference, this)

        var ta = context.obtainStyledAttributes(attrs, R.styleable.SimplePreferenceView)
        imageDrawable = ta.getDrawable(R.styleable.SimplePreferenceView_android_src)
        ta.recycle()

        @SuppressLint("CustomViewStyleable")
        ta = context.obtainStyledAttributes(attrs, R.styleable.View)
        descriptionText = ta.getText(R.styleable.View_descriptionText)
        titleText = ta.getText(R.styleable.View_titleText)
        ta.recycle()
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun refresh() {
        // intentionally empty, children can override
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        title.isEnabled = enabled
        description.isEnabled = enabled
    }

}

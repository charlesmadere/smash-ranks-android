package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Parcelable
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.extensions.clear
import com.garpr.android.misc.Refreshable
import kotterknife.bindView

open class SimplePreferenceView : LifecycleFrameLayout, Refreshable {

    private var _iconDrawable: Drawable? = null
    private var _descriptionText: CharSequence? = null
    private var _titleText: CharSequence? = null

    private val icon: ImageView by bindView(R.id.icon)
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
        get() = description.text.toString()
        set(text) {
            _descriptionText = text
            description.text = text
        }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        LayoutInflater.from(context).inflate(R.layout.view_simple_preference, this)

        val iconDrawable = _iconDrawable

        if (iconDrawable == null) {
            icon.clear()
            icon.visibility = View.GONE
        } else {
            icon.setImageDrawable(iconDrawable)
            icon.visibility = View.VISIBLE
        }

        title.text = _titleText
        description.text = _descriptionText
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SimplePreferenceView)
        _iconDrawable = ta.getDrawable(R.styleable.SimplePreferenceView_simpleIcon)
        _descriptionText = ta.getText(R.styleable.SimplePreferenceView_simpleDescriptionText)
        _titleText = ta.getText(R.styleable.SimplePreferenceView_simpleTitleText)
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
        get() = title.text.toString()
        set(text) {
            _titleText = text
            title.text = text
        }

}

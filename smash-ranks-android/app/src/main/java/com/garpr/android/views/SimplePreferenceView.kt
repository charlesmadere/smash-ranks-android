package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.support.annotation.AttrRes
import android.support.annotation.StringRes
import android.support.annotation.StyleRes
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.Refreshable
import kotterknife.bindView

open class SimplePreferenceView : FrameLayout, Heartbeat, Refreshable {

    private var mDescriptionText: CharSequence? = null
    private var mTitleText: CharSequence? = null

    private val mDescription: TextView by bindView(R.id.description)
    private val mTitle: TextView by bindView(R.id.title)


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        parseAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        parseAttributes(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        parseAttributes(attrs)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun isAlive(): Boolean {
        return ViewCompat.isAttachedToWindow(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        LayoutInflater.from(context).inflate(R.layout.view_simple_preference, this)

        mTitle.text = mTitleText
        mDescription.text = mDescriptionText
    }

    private fun parseAttributes(attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SimplePreferenceView)
        mDescriptionText = ta.getText(R.styleable.SimplePreferenceView_simpleDescriptionText)
        mTitleText = ta.getText(R.styleable.SimplePreferenceView_simpleTitleText)
        ta.recycle()
    }

    override fun refresh() {
        // intentionally empty, children can override
    }

    fun setDescriptionText(descriptionText: CharSequence?) {
        mDescriptionText = descriptionText
        mDescription.text = mDescriptionText
    }

    fun setDescriptionText(@StringRes descriptionTextResId: Int) {
        setDescriptionText(resources.getText(descriptionTextResId))
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        mTitle.isEnabled = enabled
        mDescription.isEnabled = enabled
    }

    fun setTitleText(titleText: CharSequence?) {
        mTitleText = titleText
        mTitle.text = mTitleText
    }

    fun setTitleText(@StringRes titleTextResId: Int) {
        setTitleText(resources.getText(titleTextResId))
    }

}

package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.extensions.clear
import com.garpr.android.misc.Heartbeat
import com.garpr.android.misc.Refreshable
import com.garpr.android.preferences.Preference
import kotterknife.bindView

class CheckablePreferenceView : FrameLayout, Heartbeat,
        Preference.OnPreferenceChangeListener<Boolean>, Refreshable, View.OnClickListener {

    private var mDisabledDescriptionText: CharSequence? = null
    private var mEnabledDescriptionText: CharSequence? = null
    private var mTitleText: CharSequence? = null
    private var mCheckableType: Int = 0
    private var mPreference: Preference<Boolean>? = null

    private val mCheckable: CompoundButton by bindView(R.id.icon)
    private val mDescription: TextView by bindView(R.id.description)
    private val mTitle: TextView by bindView(R.id.title)


    companion object {
        private const val CHECKABLE_TYPE_CHECKBOX = 0
        private const val CHECKABLE_TYPE_SWITCH_COMPAT = 1
    }

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

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>?) {
        dispatchThawSelfOnly(container)
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>?) {
        dispatchFreezeSelfOnly(container)
    }

    override val isAlive: Boolean
        get() = ViewCompat.isAttachedToWindow(this)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        mPreference?.addListener(this)
        refresh()
    }

    override fun onClick(v: View) {
        val preference = mPreference ?: return
        val value = preference.get()

        if (value == true) {
            preference.set(false)
        } else if (value == false) {
            preference.set(true)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mPreference?.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        val layoutInflater = LayoutInflater.from(context)

        when (mCheckableType) {
            CHECKABLE_TYPE_CHECKBOX -> layoutInflater.inflate(
                    R.layout.view_checkbox_preference, this)

            CHECKABLE_TYPE_SWITCH_COMPAT -> layoutInflater.inflate(
                    R.layout.view_switch_compat_preference, this)

            else -> throw RuntimeException("mCheckableType is an illegal value: $mCheckableType")
        }

        setOnClickListener(this)

        if (isInEditMode) {
            mTitle.text = mTitleText
            mDescription.text = mEnabledDescriptionText
        }

        if (mDisabledDescriptionText.isNullOrBlank() || mEnabledDescriptionText.isNullOrBlank()) {
            val layoutParams = mTitle.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
            mTitle.layoutParams = layoutParams
            mDescription.visibility = View.GONE
        }
    }

    override fun onPreferenceChange(preference: Preference<Boolean>) {
        if (isAlive) {
            refresh()
        }
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CheckablePreferenceView)
        mDisabledDescriptionText = ta.getText(R.styleable.CheckablePreferenceView_disabledDescriptionText)
        mEnabledDescriptionText = ta.getText(R.styleable.CheckablePreferenceView_enabledDescriptionText)
        mTitleText = ta.getText(R.styleable.CheckablePreferenceView_titleText)
        mCheckableType = ta.getInt(R.styleable.CheckablePreferenceView_checkableType, CHECKABLE_TYPE_CHECKBOX)
        ta.recycle()
    }

    override fun refresh() {
        val preference = mPreference

        if (preference == null) {
            mTitle.clear()
            mDescription.clear()
        } else {
            preference.addListener(this)
            mTitle.text = mTitleText

            if (preference.get() == true) {
                mDescription.text = mEnabledDescriptionText
                mCheckable.isChecked = true
            } else {
                mDescription.text = mDisabledDescriptionText
                mCheckable.isChecked = false
            }
        }
    }

    fun set(preference: Preference<Boolean>?) {
        mPreference?.removeListener(this)
        mPreference = preference
        refresh()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        mTitle.isEnabled = enabled
        mDescription.isEnabled = enabled
        mCheckable.isEnabled = enabled
    }

}

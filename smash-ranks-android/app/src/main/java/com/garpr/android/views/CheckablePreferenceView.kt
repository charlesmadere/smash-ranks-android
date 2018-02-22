package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.extensions.clear
import com.garpr.android.misc.Refreshable
import com.garpr.android.preferences.Preference
import kotterknife.bindView

open class CheckablePreferenceView : LifecycleFrameLayout,
        Preference.OnPreferenceChangeListener<Boolean>, Refreshable, View.OnClickListener {

    private var disabledDescriptionText: CharSequence? = null
    private var enabledDescriptionText: CharSequence? = null
    private var titleText: CharSequence? = null
    private var checkableType: Int = 0

    private val checkable: CompoundButton by bindView(R.id.checkable)
    private val description: TextView by bindView(R.id.description)
    private val title: TextView by bindView(R.id.title)


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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        preference?.addListener(this)
        refresh()
    }

    override fun onClick(v: View) {
        val preference = this.preference ?: return
        val value = preference.get()

        if (value == true) {
            preference.set(false)
        } else if (value == false) {
            preference.set(true)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        preference?.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        val layoutInflater = LayoutInflater.from(context)

        when (checkableType) {
            CHECKABLE_TYPE_CHECKBOX -> layoutInflater.inflate(
                    R.layout.view_checkbox_preference, this)

            CHECKABLE_TYPE_SWITCH_COMPAT -> layoutInflater.inflate(
                    R.layout.view_switch_compat_preference, this)

            else -> throw RuntimeException("checkableType is an illegal value: $checkableType")
        }

        setOnClickListener(this)

        if (isInEditMode) {
            title.text = titleText
            description.text = enabledDescriptionText
        }

        if (disabledDescriptionText.isNullOrBlank() || enabledDescriptionText.isNullOrBlank()) {
            val layoutParams = title.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
            title.layoutParams = layoutParams
            description.visibility = View.GONE
        }
    }

    override fun onPreferenceChange(preference: Preference<Boolean>) {
        if (isAlive) {
            refresh()
        }
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        var ta = context.obtainStyledAttributes(attrs, R.styleable.View)
        enabledDescriptionText = ta.getText(R.styleable.View_descriptionText)
        titleText = ta.getText(R.styleable.View_titleText)
        ta.recycle()

        ta = context.obtainStyledAttributes(attrs, R.styleable.CheckablePreferenceView)
        checkableType = ta.getInt(R.styleable.CheckablePreferenceView_checkableType, CHECKABLE_TYPE_CHECKBOX)
        disabledDescriptionText = ta.getText(R.styleable.CheckablePreferenceView_disabledDescriptionText)
        ta.recycle()
    }

    var preference: Preference<Boolean>? = null
        set(value) {
            field?.removeListener(this)
            field = value
            refresh()
        }

    override fun refresh() {
        val preference = this.preference

        if (preference == null) {
            title.clear()
            description.clear()
            checkable.isChecked = false
        } else {
            preference.addListener(this)
            title.text = titleText

            if (preference.get() == true) {
                description.text = enabledDescriptionText
                checkable.isChecked = true
            } else {
                description.text = disabledDescriptionText
                checkable.isChecked = false
            }
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        title.isEnabled = enabled
        description.isEnabled = enabled
        checkable.isEnabled = enabled
    }

}

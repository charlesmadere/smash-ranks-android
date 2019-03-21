package com.garpr.android.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.widget.CompoundButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.R
import com.garpr.android.extensions.clear
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.requireViewByIdCompat
import com.garpr.android.misc.Refreshable
import com.garpr.android.preferences.Preference
import kotlinx.android.synthetic.main.view_checkbox_preference.view.*

class CheckablePreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleConstraintLayout(context, attrs), Preference.OnPreferenceChangeListener<Boolean>,
        Refreshable, View.OnClickListener {

    private val disabledDescriptionText: CharSequence?
    private val descriptionText: CharSequence?
    private val titleText: CharSequence?

    companion object {
        private const val CHECKABLE_TYPE_CHECKBOX = 0
        private const val CHECKABLE_TYPE_SWITCH_COMPAT = 1
    }

    init {
        var ta = context.obtainStyledAttributes(attrs, R.styleable.CheckablePreferenceView)
        val checkableType = ta.getInt(R.styleable.CheckablePreferenceView_checkableType,
                CHECKABLE_TYPE_CHECKBOX)

        when (checkableType) {
            CHECKABLE_TYPE_CHECKBOX -> layoutInflater.inflate(
                    R.layout.view_checkbox_preference, this)

            CHECKABLE_TYPE_SWITCH_COMPAT -> layoutInflater.inflate(
                    R.layout.view_switch_compat_preference, this)

            else -> throw RuntimeException("checkableType is an illegal value: $checkableType")
        }

        disabledDescriptionText = ta.getText(R.styleable.CheckablePreferenceView_disabledDescriptionText)
        ta.recycle()

        @SuppressLint("CustomViewStyleable")
        ta = context.obtainStyledAttributes(attrs, R.styleable.View)
        descriptionText = ta.getText(R.styleable.View_descriptionText)
        titleText = ta.getText(R.styleable.View_titleText)
        ta.recycle()
    }

    private val checkable: CompoundButton
        get() = requireViewByIdCompat(R.id.checkable)

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
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

        setOnClickListener(this)

        if (isInEditMode) {
            title.text = titleText
            description.text = descriptionText
        }

        if (disabledDescriptionText.isNullOrBlank()) {
            val layoutParams = title.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.bottomMargin = layoutParams.topMargin
            title.layoutParams = layoutParams
            description.visibility = View.GONE
        }
    }

    override fun onPreferenceChange(preference: Preference<Boolean>) {
        if (isAlive) {
            refresh()
        }
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
                description.text = descriptionText
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

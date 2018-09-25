package com.garpr.android.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.R
import com.garpr.android.extensions.clear
import com.garpr.android.misc.Refreshable
import com.garpr.android.preferences.Preference
import kotterknife.bindView

class CheckablePreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleConstraintLayout(context, attrs), Preference.OnPreferenceChangeListener<Boolean>,
        Refreshable, View.OnClickListener {

    private val disabledDescriptionText: CharSequence?
    private val enabledDescriptionText: CharSequence?
    private val titleText: CharSequence?
    private val checkableType: Int

    private val checkable: CompoundButton by bindView(R.id.checkable)
    private val description: TextView by bindView(R.id.description)
    private val title: TextView by bindView(R.id.title)


    companion object {
        private const val CHECKABLE_TYPE_CHECKBOX = 0
        private const val CHECKABLE_TYPE_SWITCH_COMPAT = 1
    }

    init {
        @SuppressLint("CustomViewStyleable")
        var ta = context.obtainStyledAttributes(attrs, R.styleable.View)
        enabledDescriptionText = ta.getText(R.styleable.View_descriptionText)
        titleText = ta.getText(R.styleable.View_titleText)
        ta.recycle()

        ta = context.obtainStyledAttributes(attrs, R.styleable.CheckablePreferenceView)
        checkableType = ta.getInt(R.styleable.CheckablePreferenceView_checkableType, CHECKABLE_TYPE_CHECKBOX)
        disabledDescriptionText = ta.getText(R.styleable.CheckablePreferenceView_disabledDescriptionText)
        ta.recycle()
    }

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

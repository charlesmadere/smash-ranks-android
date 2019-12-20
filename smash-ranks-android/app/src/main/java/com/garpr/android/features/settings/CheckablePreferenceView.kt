package com.garpr.android.features.settings

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.widget.Checkable
import android.widget.CompoundButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.garpr.android.R
import com.garpr.android.extensions.layoutInflater
import kotlinx.android.synthetic.main.view_checkbox_preference.view.*

class CheckablePreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), Checkable, View.OnClickListener {

    private val disabledDescriptionText: CharSequence?
    private val descriptionText: CharSequence?

    private val checkable: CompoundButton
        get() {
            return if (checkbox.visibility == VISIBLE) {
                checkbox
            } else {
                switchCompat
            }
        }

    var listener: Listener? = null

    interface Listener {
        fun onClick(v: CheckablePreferenceView)
    }

    init {
        layoutInflater.inflate(R.layout.view_checkbox_preference, this)

        var ta = context.obtainStyledAttributes(attrs, R.styleable.CheckablePreferenceView)
        val checkableTypeOrdinal = ta.getInt(R.styleable.CheckablePreferenceView_checkableType,
                CheckableType.CHECKBOX.ordinal)

        when (CheckableType.values()[checkableTypeOrdinal]) {
            CheckableType.CHECKBOX -> {
                switchCompat.visibility = GONE
                checkbox.visibility = VISIBLE
            }

            CheckableType.SWITCH_COMPAT -> {
                checkbox.visibility = GONE
                switchCompat.visibility = VISIBLE
            }
        }

        disabledDescriptionText = ta.getText(R.styleable.CheckablePreferenceView_disabledDescriptionText)
        ta.recycle()

        @SuppressLint("CustomViewStyleable")
        ta = context.obtainStyledAttributes(attrs, R.styleable.View)
        descriptionText = ta.getText(R.styleable.View_descriptionText)
        val titleText = ta.getText(R.styleable.View_titleText)
        ta.recycle()

        title.text = titleText
        description.text = descriptionText
        setOnClickListener(this)

        if (descriptionText.isNullOrBlank()) {
            val layoutParams = title.layoutParams as LayoutParams
            layoutParams.bottomToBottom = LayoutParams.PARENT_ID
            layoutParams.bottomMargin = layoutParams.topMargin
            title.layoutParams = layoutParams
            description.visibility = GONE
        }
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun isChecked(): Boolean {
        return checkable.isChecked
    }

    override fun onClick(v: View) {
        listener?.onClick(this)
    }

    override fun setChecked(checked: Boolean) {
        description.text = if (checked || descriptionText.isNullOrBlank() ||
                disabledDescriptionText.isNullOrBlank()) {
            descriptionText
        } else {
            disabledDescriptionText
        }

        checkable.isChecked = checked
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        title.isEnabled = enabled
        description.isEnabled = enabled
        checkable.isEnabled = enabled
    }

    override fun toggle() {
        checkable.toggle()
    }

    private enum class CheckableType {
        CHECKBOX, SWITCH_COMPAT
    }

}

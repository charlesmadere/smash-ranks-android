package com.garpr.android.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.NightModeManager
import javax.inject.Inject

class ThemeOneLinePreferenceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : OneLinePreferenceView(context, attrs), NightModeManager.OnNightModeChangeListener,
        View.OnClickListener {

    @Inject
    protected lateinit var nightModeManager: NightModeManager

    init {
        titleText = context.getText(R.string.theme)

        if (isInEditMode) {
            descriptionText = context.getText(R.string.system)
        }

        imageDrawable = ContextCompat.getDrawable(context, R.drawable.ic_chevron_down_white_18dp)

        setOnClickListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        nightModeManager.addListener(this)
    }

    override fun onClick(v: View) {
        // TODO
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        nightModeManager.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            appComponent.inject(this)
            nightModeManager.addListener(this)
            refresh()
        }
    }

    override fun onNightModeChange(nightModeManager: NightModeManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        descriptionText = context.getText(nightModeManager.nightMode.textResId)
    }

}

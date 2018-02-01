package com.garpr.android.views

import android.content.Context
import android.support.annotation.AttrRes
import android.util.AttributeSet
import com.garpr.android.App
import com.garpr.android.extensions.getAttrColor
import com.garpr.android.extensions.setTintedImageDrawable
import com.garpr.android.misc.Refreshable
import com.garpr.android.models.NightMode
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.preferences.Preference
import javax.inject.Inject

class ThemeTintedImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
) : LifecycleImageView(context, attrs, defStyleAttr),
        Preference.OnPreferenceChangeListener<NightMode>, Refreshable {

    @Inject
    protected lateinit var generalPreferenceStore: GeneralPreferenceStore


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (!isInEditMode) {
            generalPreferenceStore.nightMode.addListener(this)
        }

        refresh()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        generalPreferenceStore.nightMode.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
            generalPreferenceStore.nightMode.addListener(this)
        }

        refresh()
    }

    override fun onPreferenceChange(preference: Preference<NightMode>) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        setTintedImageDrawable(context.getAttrColor(android.R.attr.textColorSecondary))
    }

}

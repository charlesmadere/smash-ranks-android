package com.garpr.android.views

import android.content.Context
import android.support.annotation.AttrRes
import android.util.AttributeSet
import com.garpr.android.App
import com.garpr.android.misc.Refreshable
import com.garpr.android.models.NightMode
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.preferences.Preference
import javax.inject.Inject

open class ThemeTintedImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
) : LifecycleImageView(context, attrs, defStyleAttr),
        Preference.OnPreferenceChangeListener<NightMode>, Refreshable {

    @Inject
    protected lateinit var generalPreferenceStore: GeneralPreferenceStore


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        generalPreferenceStore.nightMode.addListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        generalPreferenceStore.nightMode.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        App.get().appComponent.inject(this)
    }

    override fun onPreferenceChange(preference: Preference<NightMode>) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        TODO("not implemented")
    }

}
